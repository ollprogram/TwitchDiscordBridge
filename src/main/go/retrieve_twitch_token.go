/*
 * Copyright © 2025 ollprogram
 * This file is part of TwitchDiscordBridge.
 * TwitchDiscordBridge is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or \(at your option\) any later version.
 * TwitchDiscordBridge is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with TwitchDiscordBridge.
 * If not, see https://www.gnu.org/licenses.
 */

package main

import (
	"fmt"
	"log"
	"net/http"
	"net/url"
	"os"
	"os/exec"
	"runtime"
	"strings"
)

const (
	port = 3000
)

/*
*

	open the URL in the default browser of the environment
*/
func openBrowser(url string) error {
	var cmd string
	var args []string
	fmt.Printf("Opening your browser to %s...\n", url)
	switch runtime.GOOS {
	case "windows":
		cmd = "rundll32"
		args = []string{"url.dll,FileProtocolHandler", url}
	case "darwin":
		cmd = "open"
	default: // "linux", "freebsd", "openbsd", "netbsd"
		if isWSL() {
			cmd = "rundll32.exe"
			args = []string{"url.dll,FileProtocolHandler", url}
		} else {
			cmd = "xdg-open"
			args = []string{url}
		}
	}
	return exec.Command(cmd, args...).Start()
}

/*
*
Check if it's a WSL environment
*/
func isWSL() bool {
	releaseData, err := exec.Command("uname", "-r").Output()
	if err != nil {
		return false
	}
	return strings.Contains(strings.ToLower(string(releaseData)), "microsoft")
}

func main() {
	argsWithoutProg := os.Args[1:]
	if len(argsWithoutProg) < 1 {
		log.Fatal("Missing client id")
		return
	}
	clientId := argsWithoutProg[0]
	callbackURL := fmt.Sprintf("http://localhost:%d/token", port)
	targetURLObj, _ := url.Parse("https://id.twitch.tv/oauth2/authorize")
	params := url.Values{}
	params.Add("response_type", "token")
	params.Add("client_id", clientId)
	params.Add("scope", "chat:edit chat:read user:bot")
	params.Add("redirect_uri", callbackURL)
	targetURLObj.RawQuery = params.Encode()
	targetURL := targetURLObj.String()
	err := openBrowser(targetURL)
	if err != nil {
		panic(err)
	}

	fmt.Printf("Waiting for callback on %s...\n", callbackURL)

	server := &http.Server{Addr: fmt.Sprintf(":%d", port)}

	//Catch the token in the URL fragment (nat catchable from the server) and send it back to the server
	http.HandleFunc("/token", func(w http.ResponseWriter, r *http.Request) {
		_, err := fmt.Fprint(w, "<script>"+
			"const hash = window.location.hash.substr(1);"+
			"const params = new URLSearchParams(hash);"+
			"const token = params.get('access_token');"+
			"window.location.assign('/token_data?access_token=' + token);"+
			"</script>"+
			"</html>")
		if err != nil {
			log.Fatalf("Server response error : %s", err.Error())
			return
		}
	})

	//Retrieve the token in the query params and send it to the client
	http.HandleFunc("/token_data", func(w http.ResponseWriter, r *http.Request) {
		token := r.URL.Query().Get("access_token")
		_, err := fmt.Fprintf(w, "<!DOCTYPE html>"+
			"<html><head><meta charset=\"UTF-8\"></head>"+
			"<p> Retrieved token : %s </p><p>You may close this page.</p>"+
			"</html>", token)
		if err != nil {
			log.Fatalf("Server response error : %s", err.Error())
			return
		}
		fmt.Printf("Token retrieved : %s\n", token)
		go func() {
			err := server.Close()
			if err != nil {
				log.Fatalf("Server error on close : %s", err.Error())
			}
		}()
	})

	if err := server.ListenAndServe(); err != nil && err != http.ErrServerClosed {
		log.Fatalf("Server error: %v", err)
	}
}

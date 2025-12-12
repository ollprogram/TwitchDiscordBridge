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

package fr.ollprogram.twitchdiscordbridge.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageUtilsTest {


    @Test
    void filterMessageHTTPURL() {
        assertEquals("[url]", MessageUtils.filterMessage("http://www.chess.com/home"));
    }

    @Test
    void filterMessageHTTPSURL() {
        assertEquals("[url]", MessageUtils.filterMessage("https://www.chess.com/home"));
    }

    @Test
    void filterMessageWLink() {
        assertEquals("[url]", MessageUtils.filterMessage("www.chess.com/home"));
    }

    @Test
    void filterMessageDomainLink() {
        assertEquals("[url]", MessageUtils.filterMessage("chess.com/home"));
    }

    @Test
    void filterMessageDomainLink2() {
        assertEquals("[url]", MessageUtils.filterMessage("chess.com"));
    }

    @Test
    void filterMessageComplexWithLinks() {
        assertEquals("Hello my name is ollprogram and i like playing [url], also look my code on [url] or [url] or [url]", MessageUtils.filterMessage("Hello my name is ollprogram and i like playing chess.com, also look my code on https://github.com or http://github.com or github.com"));
    }
}
/*
 * PingPong.go
 *
 * The standard ping pong example in Go
 *
 * Copyright (c) 2018-2021 HS Emden-Leer
 * All Rights Reserved.
 *
 * @version 2.00 - 13 May 2021 - GJV - introduced Message type, cmdChannel
 * @version 1.00 - 17 Jan 2018 - GJV - initial version
 */

package main

import "fmt"

type Message string

const maxExchanges = 50 // the number of messages exchanged

const ( // the possible messages
	startMsg Message = "Start"
	doneMsg  Message = "Done"
	pingMsg  Message = "Ping"
	pongMsg  Message = "Pong"
)

func main() {
	fmt.Printf("ping pong, world\n")

	var ppChannel = make(chan Message)     // channel between players
	var cmdChannel = make(chan Message)    // command channel for main program
	go ping(ppChannel, cmdChannel)         // set up first player (Ping)
	go pong(ppChannel)                     // set up second player (Pong)
	cmdChannel <- startMsg                 // let the games begin!
	fmt.Printf("main: %s\n", <-cmdChannel) // wait for Done Message
}

func ping(ppChannel chan Message, cmdChannel chan Message) {
	var pongCnt = 0

	msg := <-cmdChannel  // wait for start message
	if msg == startMsg { // only continue after start message
		ppChannel <- pingMsg // send initial message to the other player
		for {
			switch msg := <-ppChannel; { // listen on ping pong channel
			case msg == pongMsg: // received Pong message?
				pongCnt++ // increase the counter
				fmt.Printf("ping: received pong message #%d\n", pongCnt)
				if pongCnt < maxExchanges { // not reached the limit yet?
					ppChannel <- pingMsg // send another Ping message
				} else {
					ppChannel <- doneMsg  // inform other player to close down
					cmdChannel <- doneMsg // inform main program to close down
					fmt.Printf("ping: closing down\n")
					return // leave goroutine
				}
			default:
				fmt.Printf("ping: unexpected message: '%s'!\n", msg)
			}
		}
	}
}

func pong(ppChannel chan Message) {
	for {
		switch msg := <-ppChannel; { // listen on ping pong channel
		case msg == pingMsg: // received a Ping message?
			fmt.Printf("pong: received a ping message!\n")
			ppChannel <- pongMsg // send a Pong message back
		case msg == doneMsg: // received a Done message?
			// will not be shown if main closes down earlier!
			fmt.Printf("pong: closing down\n")
			return // leave goroutine
		default:
			fmt.Printf("pong: unexpected message: '%s'!\n", msg)
		}
	}
}

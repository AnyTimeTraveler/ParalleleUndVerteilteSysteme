/*
 * abp.go
 *
 * The Alternating Bit Protocol in Go
 *
 * Copyright (c) 2018-2019 HS Emden-Leer
 * All Rights Reserved.
 *
 * @version 2.00 - 23 Jan 2019 - GJV - condense sender & receiver
 * @version 1.00 - 18 Dec 2018 - GJV - initial version
 */
//

package main

import (
	"fmt"
	"log"
	"math/rand"
)

// some debugging support
const debug debugging = !true // or flip to false

type debugging bool

func (d debugging) Printf(format string, args ...interface{}) {
	if d {
		log.Printf(format, args...)
	}
}

// definition of a bit extended with an error state
type bit int

const (
	error  bit = -1 // additional error state
	aState bit = 0
	bState bit = 1
)

func (b bit) flip() bit { // function to be applied to a bit (cf. method)
	switch b {
	case aState:
		return bState
	case bState:
		return aState
	default:
		return error
	}
}

func (b bit) String() string { // Java toString()-equivalent
	switch b {
	case aState:
		return "A-State"
	case bState:
		return "B-State"
	default:
		return "Error"
	}
}

// data to be transferred are Unicode characters
type (
	data = rune
)

// combines a data element with the checking alternating bit
type frame struct {
	state bit
	info  data
}

const failurePercentage = 25 // percentage of corrupted channel transmissions

const dataMessage = "This is the Alternating Bit Protocol! 👏" // Unicode!
const eom = 0                                                 // end of message

var quit = make(chan bool) // the quit channel

func main() {
	fmt.Printf("The Alternating Bit Protocol\n")

	var sysIn = make(chan data) // system input & output
	var sysOut = make(chan data)

	var dcIn = make(chan frame) // data channel input & output
	var dcOut = make(chan frame)

	var acIn = make(chan bit) // acknowledgement channel input & output
	var acOut = make(chan bit)

	go sender(sysIn, dcIn, acOut)
	go dataChannel(dcIn, dcOut)
	go receiver(dcOut, sysOut, acIn)
	go ackChannel(acIn, acOut)
	go producer(sysIn)  // delivers input to the ABP
	go consumer(sysOut) // receives output from the ABP

	<-quit // wait for a signal on the quit channel
	fmt.Printf("main: closing\n")
}

// feeds the ABP with individual characters from the input string
func producer(out chan<- data) {
	for _, char := range dataMessage {
		debug.Printf("Producer character %c\n", char)
		out <- char
	}
	out <- eom // signal end of message
}

// consumes the individual characters from the ABP and shows them
func consumer(in <-chan data) {
	for {
		char := <-in
		if char == eom { // end of message?
			quit <- true // signal exit to main
			return       // exit function
		}
		fmt.Printf("Consumer character: %c\n", char)
	}
}

// determines whether a simulated failure occurs
func failureOccurred(failurePercentage int) bool {
	return rand.Intn(100) < failurePercentage
}

// the sender of the ABP
func sender(in <-chan data, out chan<- frame, ack <-chan bit) {

	sendFrame := func(bit, data) {} // forward declaration

	// inner function
	sendFrame = func(b bit, d data) { // redefinition
		debug.Printf("sendFrame: %s %c \n", b, d)
		out <- frame{b, d} // construct the actual frame and send it
		// waitForAck(b, d)
		debug.Printf("waitForAck: %s %c \n", b, d)
		ackMsg := <-ack
		if ackMsg != b {
			sendFrame(b, d)
		}
	}

	// sender main loop
	for bit := aState; ; bit = bit.flip() { // start with bit in aState
		d := <-in
		sendFrame(bit, d)
	}
}

// the data channel used to connect the sender to the receiver
func dataChannel(in <-chan frame, out chan<- frame) {
	for {
		f := <-in
		if failureOccurred(failurePercentage) {
			f.state = error // corrupt the frame
		}
		out <- f
	}
}

// the receiver of the ABP
func receiver(in <-chan frame, out chan<- data, ack chan<- bit) {
	state := aState // start with the a-State

	receiveFrame := func(bit) {} // forward declaration

	// inner function
	receiveFrame = func(b bit) { // redefinition
		f := <-in
		debug.Printf("receiveFrame in state %s: %s %c \n", b, f.state, f.info)
		if f.state == b {
			out <- f.info
			ack <- b
		} else {
			// retry(b)
			debug.Printf("retry: %s \n", b)
			ack <- b.flip()
			receiveFrame(b)
		}
	}

	// receiver main loop
	for {
		receiveFrame(state)
		state = state.flip() // now alternate the bit!
	}
}

// the acknowledgement channel used to connect the receiver to the sender
func ackChannel(in <-chan bit, out chan<- bit) {
	for {
		b := <-in
		if failureOccurred(failurePercentage) {
			b = error // corrupt the acknowledgement
		}
		out <- b
	}
}

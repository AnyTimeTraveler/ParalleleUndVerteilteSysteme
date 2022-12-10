/*
 * fibonacci.go
 *
 * Example of select statment in Go
 *
 * culled from: https://tour.golang.org/concurrency/5
 * Copyright (c) Google LLC
 *
 * Adaptations
 * Copyright (c) 2020-2020 HS Emden-Leer
 * All Rights Reserved.
 *
 * @version 1.00 - 09 Dec 2020 - GJV - initial version
 */

package main

import (
	"fmt"
)

func fibonacci(c, quit chan int) {
	x, y := 0, 1
	for {
		select {
		case c <- x:
			x, y = y, x+y
		case <-quit:
			fmt.Println("quit")
			return
		}
	}
}

func main() {
	c := make(chan int)
	quit := make(chan int)

	go fibonacci(c, quit)

	for i := 0; i < 10; i++ {
		fmt.Println(<-c)
	}
	quit <- 0 // send quit message
}

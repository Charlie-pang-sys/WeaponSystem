package main

import (
	"fmt"
	"time"
)

func printRoutine() {
	fmt.Println("马士兵教育申专你好！")
	time.Sleep(1 * time.Second)
}

func main() {
	for i := 0; i < 10; i++ {
		go printRoutine()
		time.Sleep(2 * time.Second)
	}
	fmt.Println("main结束")
}

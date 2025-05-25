package main

import (
	"fmt"
	"time"
)

// 子Goroutine
func newTask() {
	//创建一个死循环
	i := 0
	for {
		i++
		fmt.Printf("new Goroutine :i =%d\n", i)
		time.Sleep(1 * time.Second)
	}
}

func main() {
	//创建一个go程，去执行newTask()
	go newTask()

	fmt.Println("这是一个主遥测...")

	// fmt.Println("main goroutine exit")

	//创建一个死循环
	i := 0
	for {
		i++
		fmt.Printf("main goroutine:i =%d\n", i)
		time.Sleep(1 * time.Second)
	}
}

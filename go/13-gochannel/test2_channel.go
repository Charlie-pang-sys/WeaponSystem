package main

import (
	"fmt"
	"time"
)

func main() {
	//定义有缓存的channel，长度为3
	c := make(chan int, 3)
	num := 3
	go func(num int) {
		defer fmt.Println("goroutine结束")
		for i := 0; i < num; i++ {
			fmt.Println("当前的i是：", i, "当前的len是", len(c), "当前的cap是", cap(c))
			c <- i
		}
	}(num)
	time.Sleep(time.Second * 1)
	for i := 0; i < 3; i++ {
		num1 := <-c
		fmt.Println("num1=", num1)
	}
	fmt.Println("main结束")
}

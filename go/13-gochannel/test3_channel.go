package main

import (
	"fmt"
	"time"
)

func main() {
	c := make(chan int)
	go func() {
		for i := 0; i < 5; i++ {
			fmt.Println("goroutine", i)
			c <- i
		}
		close(c)
	}()
	time.Sleep(time.Second * 1)
	//方式一、从channel中获取数据的
	/*for {
		if num, ok := <-c; ok {
			fmt.Println("num=", num)
		} else {
			fmt.Println("channel is closed")
			break
		}
	}
	*/
	//方式二、从channel中获取数据的
	for data := range c {
		fmt.Println("data=", data)
	}
	fmt.Println("main结束")
}

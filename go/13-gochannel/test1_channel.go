package main

import (
	"fmt"
)

func main() {
	//定义一个无缓存的channel
	c := make(chan int)

	go func(a int, b int) {
		//这行代码永远在main goroutine的num := <-c之后运行
		defer fmt.Println("goroutine结束")

		fmt.Println("goroutine正在运行...")

		c <- a * b
	}(2, 3)
	fmt.Println("main goroutine开始")
	num := <-c
	fmt.Println("num = ", num)
	fmt.Println("main goroutine结束")
}

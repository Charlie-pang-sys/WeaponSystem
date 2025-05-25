package main

import (
	"fmt"
	"time"
)

func main() {
	//创建一个匿名的go程，用go创建承载一个形参为空，返回值为空的函数
	go func() {
		defer fmt.Println("A.defer")
		func() {
			defer fmt.Println("B defer")
			fmt.Println("B")
		}()

		func(num int) {
			defer fmt.Println("C defer")
			fmt.Println("num is", num)
		}(1)
		fmt.Println("A")
	}()

	for {
		time.Sleep(1 * time.Second)
	}

}

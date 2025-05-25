package main

import (
	"fmt"
)

/*
*
select可以监控多路channel的状态
*/
func fibonacii(put1, put2 chan int) {
	x, y := 1, 1
	for {
		select {
		case put1 <- x:
			fmt.Println("当前的x是：", x, "当前的y是", y)
			//当put1可以写，则该case就会进来
			x = y
			y = x + y
		case <-put2:
			//当put2可读，则该case就会进来
			fmt.Println("结束")
			return
		}
	}
}
func main() {
	put1 := make(chan int)
	put2 := make(chan int)

	go func() {
		defer fmt.Print("goroutine结束")
		for i := 0; i < 6; i++ {
			fmt.Println(<-put1)
		}
		put2 <- 0
	}()
	fibonacii(put1, put2)
}

// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract Struct{
    struct Car{
        string model;
        uint year;
        address owner;
    }
    Car public car;
    Car[] public cars;
    mapping(address => Car[]) public carsByOwner;

    function examples() external{
        Car memory Audi = Car("Audi" ,1932,msg.sender);
        //这类写法可以不按照构造参数的顺序，自定义入参
        Car memory toyota = Car({year:1980,model:"Toyota", owner:msg.sender});
        Car memory BMW;
        BMW.model = "BMW";
        BMW.owner= msg.sender;
        BMW.year = 1972;
        cars.push(Audi);
        cars.push(toyota);
        cars.push(BMW);
        
        cars.push(Car("Tesla" , 2034, msg.sender));

        //如果Car类型定义为storage，那么可以修改合约中的数据
        Car storage _car = cars[0];
        _car.year = 1999;
        //删除第0个car的owner字段，使其为默认值
        delete _car.owner;
        //删除第一个car元素
        delete cars[1];

    }
}
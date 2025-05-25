// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract DeployWithCreate2{
    address public owner;

    constructor(address _owner){
        owner = _owner;
    }
}

/**
 * 通过create2部署合约
 * 步骤
 * 1、部署Create2Factory合约
 * 2、传入任意一个地址（钱包地址），作为getByteCode方法的入参
 * 3、将2返回的bytecode作为getAddress方法的第一个入参，第二个是salt（随便填个数字）
 * 4、调用deploy方法，入参的salt必须和3一样
 */
contract Create2Factory{
    event Deploy(address addr);

    //部署合约
    function deploy(uint _salt)external{
        DeployWithCreate2 _contract =new DeployWithCreate2{
            salt:bytes32(_salt)
        }(msg.sender);
        emit Deploy(address(_contract));
    }

    //获取合约地址
    function getAddress(bytes memory bytecode,uint _salt) public view returns(address){
        bytes32 hash = keccak256(
            abi.encodePacked(
                bytes1(0xff),address(this), _salt, keccak256(bytecode)
            )
        );
        return address(uint160(uint(hash)));
    }

    //获取bytecode
    function getByteCode(address _owner) public pure returns(bytes memory){
        //通过反射获取bytecode
        bytes memory bytecode = type(DeployWithCreate2).creationCode;
        //将bytecode与构造参数一起打包
        return abi.encodePacked(bytecode,abi.encode(_owner));
    }
}
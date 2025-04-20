// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract AccessControl{
    mapping(bytes32=>mapping(address=>bool)) public roles;
    //0xdf8b4c520ffe197c5343c6f5aec59570151ef9a492f2c624fd45ddde6135ec42
    bytes32 private constant ADMIN = keccak256(abi.encodePacked("ADMIN"));
    //0x2db9fd3d099848027c2383d0a083396f6c41510d7acfd92adc99b6cffcf31e96
    bytes32 private constant USER = keccak256(abi.encodePacked("USER"));

    event RoleGranted(bytes32 indexed role,address indexed account);

    constructor(){
        roles[ADMIN][msg.sender] = true;
    }

    modifier onlyOwner(bytes32 _role){
        require(roles[_role][msg.sender],"only the owner of this contract can call it");
        _;
    }

    function _upgradeRoles(bytes32 role, address user) internal{
        roles[role][user] = true;
        emit RoleGranted(role, user);
    }

    //升级账户
    function upgradeRoles(bytes32 _role, address user)external onlyOwner(ADMIN){
        _upgradeRoles(_role,user);
    }
    //撤销权限
    function demotionRoles(bytes32 role, address user) external onlyOwner(role){
        roles[role][user] = false;
    }
}
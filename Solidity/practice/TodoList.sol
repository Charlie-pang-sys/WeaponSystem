// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

//待办事项的demo
contract TodoList{
    struct Todo{
        string text;
        bool completed;
    }

    Todo[] public todos;

    function create(string calldata _text) external{
        todos.push(Todo(_text,false));
    }

    function updateText(uint _index, string calldata _text) external{
        todos[_index].text = _text;
    }

    function get(uint _index) external view returns(string memory,bool){
        Todo memory todo = todos[_index];
        return(todo.text,todo.completed);
    }

    function updateState(uint _index) external {
        todos[_index].completed = !todos[_index].completed;
    }
}
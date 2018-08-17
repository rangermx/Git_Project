/**
 * 使用C语言实现Java的状态模式
 * author liumingxin
 * 要解决的痛点：
 *   之前项目中使用switch...case...语句 和 if...else...语句实现状态机
 *   造成逻辑处理线程代码行数过多，可读性差，可维护性差
 * 设计思想：
 *   使用StateMachine结构体模仿Java接口，元素定义为函数指针。
 *   在每个状态的.c文件中定义一个StateMachine结构体变量，将函数指针指向自己状态内定义的函数体
 *   主逻辑定义一个StateMachine结构体指针。
 *   在进行状态切换时，只需要将StateMachine结构体指针，指向相应状态的StateMachine结构体即可
 *   添加状态时，只需要新定义一对.c.h文件，新创建一个StateMachine结构体变量和相应状态的处理函数
 *   添加处理内容时，只需要在结构体中新添加一个函数指针。
***/
#include "logic.h"

StateMachine *machine;
unsigned char UsartBuffer1[USAET_BUF_LEN];
unsigned char UsartBuffer2[USAET_BUF_LEN];

void stateMachineInit()
{
    state_init_init();
    state_idle_init();
    state_unusual_init();
}

void main()
{
    stateMachineInit();// 初始化
    *machine = state_init;// 切换状态
    while (1)         // 进入状态机
    {
        // OSTIMEDELAY(); // 任务调度延时

        machine->buffer1DataHandle(UsartBuffer1, USAET_BUF_LEN); // 处理串口1数据
        machine->buffer2DataHandle(UsartBuffer1, USAET_BUF_LEN); // 处理串口2数据
        machine->GPIO_input();// IO输入处理
        // 如需切换状态，将machine指针指向其他状态即可，如*machine = state_idle; state_unusual
        // 切换状态可以在该逻辑循环中赋值显式进行，也可以在machine的逻辑处理函数中赋值隐式进行
        // 根据我的个人理解，显式、隐式各有利弊
    }
}

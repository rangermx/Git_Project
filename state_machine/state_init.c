#include "state_init.h"
extern StateMachine state_idle;
extern StateMachine *machine;
StateMachine state_init;

void state_init_buffer1_data_handle(unsigned char *buf, char len) 
{
    // 进行初始化状态下的串口1数据操作
}

void state_init_buffer2_data_handle(unsigned char *buf, char len)
{
    // 进行初始化状态下的串口2数据操作
}

void state_init_GPIO_input()
{
    // 进行初始化状态下的GPIO输入操作
    machine = &state_idle;
}

void state_init_init()
{
    state_init.buffer1DataHandle = state_init_buffer1_data_handle;
    state_init.buffer2DataHandle = state_init_buffer2_data_handle;
    state_init.GPIO_input = state_init_GPIO_input;
}

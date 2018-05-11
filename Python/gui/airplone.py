#coding=utf-8
import pygame
from pygame.locals import *

class HeroPlane(object):
    def __init__(self, screen):
        # 设置飞机的默认位置
        self.x = 230
        self.y = 700

        # 设置要显示内容的窗口
        self.screen = screen

        # 用来保存英雄飞机需要的图片的名字
        self.imageName = "./feiji/hero1.png"

        # 根据名字生成飞机图片
        self.image = pygame.image.load(self.imageName)

    def display(self):
        self.screen.blit(self.image,(self.x, self.y))

    def moveLeft(self):
        self.x -= 10
    
    def moveRight(self):
        self.x += 10
    
def key_control(heroPlane):
    # 获取事件，比如按键等
    for event in pygame.event.get():
        # 判断是否点击了退出按钮
        if event.type == QUIT:
            print("exit")
            exit()
        # 判断是否按下了键
        elif event.type == KEYDOWN:
            # 检测按键是否是a或left
            if event.key == K_a or event.key == K_LEFT:
                print('left')
                heroPlane.moveLeft()
            # 检测按键是否是d或right
            elif event.key == K_d or event.key == k_RIGHT:
                print('right')
                heroPlane.moveRight()
            # 检测按键是否是空格键
            elif event.key == K_SPACE:
                print('space')

def main():
    # 创建一个窗口，用来显示内容
    screen = pygame.display.set_mode((480,852),0,32)

    # 创建一个和窗口大小的图片，来充当背景
    background = pygame.image.loat("./feiji/background.png")

    # 创建一个飞机对象
    heroPlane = HeroPlane(screen)

    # 把背景图片放到窗口中显示
    while True:

        # 设定需要显示的背景图
        screen.blit(background,(0,0))

        heroPlane.display()
            
        key_control(heroPlane)
        
        # 更新需要显示的内容
        pygame.display.update()

if __name__ == "__main__":

    main()
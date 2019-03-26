#include <iostream>

using namespace std;

class Box {
    public:
        double length;
        double breadth;
        double height;
};

int main() {
    Box box1;
    Box box2;
    
    double volume = 0.0;
    box1.height = 5.0;
    box1.length = 6.0;
    box1.breadth = 7.0;

    box2.height = 10.0;
    box2.length = 12.0;
    box2.breadth = 13.0;
    
    // box 1 的体积
    volume = box1.height * box1.length * box1.breadth;
    cout << "Box1 的体积：" << volume <<endl;
    
    // box 2 的体积
    volume = box2.height * box2.length * box2.breadth;
    cout << "Box2 的体积：" << volume <<endl;
    return 0;
}

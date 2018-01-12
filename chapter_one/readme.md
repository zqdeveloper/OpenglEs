初识OpenGL Es 2.0
=======
本章用来记录我学习的OpenGL Es 的第一章的知识点

***

|Author|写代码的向日葵|
|---|---
|E-mail|1052105484@qq.com

***

## 目录
### 第一章 初识 OpenGL Es 2.0
* 1.1 OpenGL Es2.0概览
  * 1.1.1 OpenGL Es2.0 简介
  * 1.1.2 初识OpenGL Es2.0 应用程序
* 1.2 着色器与渲染管线
  * 1.2.1 OpenGL Es 1.x 的渲染管线
  * 1.2.2 OpenGL ES 2.0 渲染管线
  * 1.2.3 OpenGL Es 中立体物体的构建
  
*****
    
# 1.1 OpenGl Es 2.0概述
* 随着3G时代的到来，Andriod与iPhone逐渐成为消费者购买智能手机的主要选择。而由于基于Android的智能手机性能优良、价格合适，因此Android智能手机得到了大多数用户的青睐。
* 随着Android系统版本及硬件水平的提升，OpenGL Es版本也由原先仅支持固定渲染管线的OpenGL Es 1.x 升级为支持自定义渲染管线的OpenGl Es2.0 。这使得使用OpenGL Es 2.0 渲染的3D场景更加真实，从而能够创造全新的用户体验。
        
### 1.1.1 OpenGL Es 2.0简介
* 现今较为知名的3D图形API有OpenGL、DirectX以及OpenGL Es,他们各自的应用领域如下。

    1 . DirectX 主要应用于Windows下游戏的开发，再次领域基本上一统天下
   
    2 . OpenGL 的应用领域较为广泛，适用于Unix、Mac Os、Linux以及Microsoft等几乎所有的操作系统，可以开发游戏、工业建模以及嵌入式设备。
   
    3 . OpenGl Es 是专门针对于嵌入式设备而设计的，其实际是OpenGL 的裁剪版本，去除了OpenGL中许多不是必须存在的特性，如:GL_QUADS(四边形)与GL_POLYGONS(多边形)绘制模式以及glBegin（开始）glEnd(结束)操作等。

* 经过多年的发展，OpenGL Es 主要分为两个主版本，其基本情况如下。

    1 . 一个是OpenGL Es 1.x（主要包括1.0与1.1），其采用的是固定渲染管线，可以由硬件GPU支持或用软件模式实现，渲染能力有限，在纯软件模拟情况下性能也较弱。（典型的使用OpenGL ES 1.x渲染技术的游戏：都市赛车5）
    
    2 . 另一个是OpenGL ES 2.0,其采用的是可编程渲染管线，渲染能力大大提高。`OpenGL ES 2.0`  要求设备中必须有相应的GPU硬件设备的支持，目前不支持在设备上用软件模拟实现。（典型的使用OpenGL ES 2.0 渲染技术的游戏：都市赛车6）
    
***
# 1.1.2 初识OPenGL ES 2.0 应用程序
    
    
````java
public class ShaderUtil{
    public static int load
}

    

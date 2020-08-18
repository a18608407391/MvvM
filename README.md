# 本项目基于Mvvm(开发模式)+模块化(结构)+kotlin(语言环境)+okhttp（网络框架）+ retrofit(网络适配器框架)+navigation(SingleActivity-UI架构)
# 主要用于新项目开发前期架构搭建或旧项目重构，后续会引入更多组件，便于开发时效率稳定性提升。

# 项目结构
# library - 基础库，负责引入项目所需的依赖包，及其开发所需的工具类，Android组件基类的预设。
# library-common - 公共基础库，依赖基础库，负责开发过程中，公共属性的设定，全局调用
# app    -  宿主 App入口
# 其他业务层

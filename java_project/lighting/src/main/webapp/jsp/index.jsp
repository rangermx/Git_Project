<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" import="java.util.*,com.test.domain.*"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>浙江雷培德zigbee灯光智能控制系统</title>
  <meta name="viewport" content="width=device-width,initial-scale=1">
  <!--[if lte IE 8]><link rel="stylesheet" href="../jsp/responsive_nav/responsive-nav.css"><![endif]-->
  <!--[if gt IE 8]><!--><link rel="stylesheet" href="../jsp/styles.css"><!--<![endif]-->
  <script src="../jsp/responsive_nav/responsive-nav.js"></script>
  <link rel="stylesheet" href="../jsp/bootstrap/css/bootstrap.css">
  <link rel="shortcut icon" type="image/x-icon" href="../jsp/favicon.ico" />
  <script type="text/javascript" src="../jsp/jquery.min.js"></script>
  <script type="text/javascript" src="../jsp/bootstrap/js/bootstrap.min.js"></script>
  <script type="text/javascript" src="../jsp/Vue.js"></script>
  <style>
		#logoDiv{
      width:100%;
      height:80px;
      background-image:url('<%=request.getContextPath()%>/jsp/logowaho.jpg'); 
      background-repeat:no-repeat;
      background-size:100% 100%;
      -moz-background-size:100% 100%;
      /* margin-bottom:-70px; */
    }

    .table > thead > tr > th,
    .table > tbody > tr > th,
    .table > tfoot > tr > th {
      text-align: center;
    }
    .table > thead > tr > td,
    .table > tbody > tr > td,
    .table > tfoot > tr > td {
      text-align: center;
    }

    /*媒体查询：注意and后面空格的添加*/
    /*iphone: w < 768px*/
    @media screen and (max-width: 645px){
      #main_body{
        margin-top:0px;
      }
      .name-button{
        width:100px;
        overflow:hidden;
        text-overflow:ellipsis;
      }
    }
    /*中等屏幕   w >= 992  && w<1200*/
    @media screen and (max-width: 1200px) and (min-width: 645px) {
      #main_body{
        margin-top:70px;
      }
      .name-button{
        width:100px;
        overflow:hidden;
        text-overflow:ellipsis;
      }
    }
    /*大屏幕   w >= 1200*/
    @media screen and (min-width: 1200px) {
      #main_body{
        margin-top:70px;
      }
      .name-button{
        width:160px;
        overflow:hidden;
        text-overflow:ellipsis;
      }
    }
    .btn-language{
      margin-right:0px;
    }
    
	</style>
</head>
<body>
<div id="mybody">
  <!-- 组添加节点模态框（Modal） -->
  <!-- 要放到视图最外层(body下)不然产生bug-->
  <div class="modal fade" id="addToGroupM" tabindex="-1" role="dialog" aria-labelledby="addToGroupMLabel" aria-hidden="true">
      <div class="modal-dialog">
          <div class="modal-content">
              <div class="modal-header">
                  <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                  <h4 class="modal-title" id="addToGroupMLabel">{{languageFlag[0] == 0 ? "请选择要添加的节点:" : "Please select the node to add:"}}</h4>
                  <h5 class="modal-title" id="addToGroupMLabel">{{languageFlag[0] == 0 ? "点击在线集控器名称可展开节点，只可添加在线节点，每个节点最多只能被添加进16个组。" : "Click on the name of the online controller to expand the node. Only online nodes can be added. Each node can only be added to 16 groups."}}</h5>
              </div>
              <div class="modal-body">
                <form class="form-inline" role="form" onkeydown="if(event.keyCode==13)return false;">
                  <div class="panel-group" id="accordion">
                    <div class="panel panel-default" v-for="dev of user.devSet"><!-- v-if="dev.online == true" -->
                      <div class="panel-heading">
                        <h4 class="panel-title">
                          <input type="checkbox" v-bind:name="dev.mac+'box'" onclick="checkAll(this)">
                          <a data-toggle="collapse" data-parent="#accordion" 
                            v-bind:href="'#'+dev.mac+'sub'">
                            {{dev.name}}
                          </a>
                        </h4>
                      </div>
                      <div v-bind:id="dev.mac+'sub'" class="panel-collapse collapse">
                        <div class="panel-body">
                          <ul class="list-group">
                            <li class="list-group-item" v-for="zb of user.zigbeeSet" v-if="zb.devMac == dev.mac && zb.groupAddrs.indexOf(group.addr) == -1 && zb.online == true">
                              <input type="checkbox" class="zigbeeCheckbox"v-bind:class="dev.mac+'subbox'" v-bind:name="zb.mac+'box'">{{zb.name}}
                            </li>
                          </ul>
                        </div>
                      </div>
                    </div>
                  </div>
                </form>
                <script>
                  function checkAll(obj) {
                    var devMac = obj.name.substring(0,obj.name.length - 3);
                    var className = devMac + "subbox";
                    // $(className).attr("checked", obj.checked);//jquery设置有bug，checkbox被用户点击后无法被脚本设置
                    var list = document.getElementsByClassName(className);
                    for(var i = 0; i < list.length; i++){
                      list[i].checked = obj.checked;
                    }
                  }
                </script>
              </div>
              <div class="modal-footer">
                  <button type="button" class="btn btn-default" data-dismiss="modal">{{languageFlag[0] == 0 ? "取消" : "Cancel"}}</button>
                  <button type="button" class="btn btn-primary" @click="submitChange()">{{languageFlag[0] == 0 ? "提交更改" : "Submit"}}</button>
              </div>
          </div><!-- /.modal-content -->
      </div><!-- /.modal -->
  </div>
  <!-- 添加策略模态框（Modal） -->
  <div class="modal fade" id="addPloyM" tabindex="-1" role="dialog" aria-labelledby="addPloyMLabel" aria-hidden="true">
      <div class="modal-dialog">
          <div class="modal-content">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                  &times;
                </button>
                <h4 class="modal-title" id="addPloyMHeader">
                  {{header}}
                </h4>
              </div>
              <div class="modal-body">
                <form class="form-horizontal" role="form" name="addPloyForm" onkeydown="if(event.keyCode==13)return false;">
                  <div class="form-group">
                    <label class="col-sm-4 control-label">{{languageFlag[0] == 0 ? "策略名" : "Ploy name"}}</label>
                    <div class="col-sm-6">
                      <input type="text" class="form-control" placeholder="" required="required" name="ployNameInput">
                      <span style="color:red; display:none" id="ployNameSpan">{{languageFlag[0] == 0 ? "请输入策略名" : "Please enter the ploy name"}}</span>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4 control-label">{{languageFlag[0] == 0 ? "设置绑定" : "Bind group"}}</label>
                    <div class="btn-group col-sm-4">
                      <div class="btn-group">
                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                          {{bindVal.name}}
                          <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu">
                          <li v-for="item of groups">
                            <a href="javascript:void(0);" @click="selectBind(item)">{{item.name}}</a>
                          </li>
                        </ul>
                      </div>
                      <span style="color:red; display:none" id="ployBindSpan">{{languageFlag[0] == 0 ? "请绑定分组" : "Please bind a group"}}</span>
                    </div>
                  </div>
                </form>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">{{languageFlag[0] == 0 ? "取消" : "Cancel"}}</button>
                <button type="button" class="btn btn-primary" @click="submit()">
                  {{languageFlag[0] == 0 ? "提交更改" : "Submit"}}
                </button>
              </div>
          </div><!-- /.modal-content -->
      </div><!-- /.modal -->
  </div>
  <!-- 更改策略绑定模态框（Modal） -->
  <div class="modal fade" id="ployBindM" tabindex="-1" role="dialog" aria-labelledby="ployBindMLabel" aria-hidden="true">
      <div class="modal-dialog">
          <div class="modal-content">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                  &times;
                </button>
                <h4 class="modal-title" id="ployBindMHeader">
                  {{header}}
                </h4>
              </div>
              <div class="modal-body">
                <form class="form-horizontal" role="form" name="ployBindForm" onkeydown="if(event.keyCode==13)return false;">
                  <div class="form-group">
                    <label class="col-sm-4 control-label">{{languageFlag[0] == 0 ? "设置绑定" : "Bind group"}}</label>
                    <div class="btn-group col-sm-4">
                      <div class="btn-group">
                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                          {{bindVal.name}}
                          <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu">
                          <li v-for="item of groups">
                            <a href="javascript:void(0);" @click="selectBind(item)">{{item.name}}</a>
                          </li>
                        </ul>
                      </div>
                      <span style="color:red; display:none" id="ployBindSpan">{{languageFlag[0] == 0 ? "请绑定分组" : "Please bind a group"}}</span>
                    </div>
                  </div>
                </form>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">{{languageFlag[0] == 0 ? "取消" : "Cancel"}}</button>
                <button type="button" class="btn btn-primary" @click="submit()">
                  {{languageFlag[0] == 0 ? "提交更改" : "Submit"}}
                </button>
              </div>
          </div><!-- /.modal-content -->
      </div><!-- /.modal -->
  </div>
  <!-- 策略指令编辑模态框（Modal） -->
  <div class="modal fade" id="operateEditM" tabindex="-1" role="dialog" aria-labelledby="operateEditMLabel" aria-hidden="true">
      <div class="modal-dialog">
          <div class="modal-content">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                  &times;
                </button>
                <h4 class="modal-title" id="operateEditMHeader">
                  {{header}}
                </h4>
              </div>
              <div class="modal-body">
                <form class="form-horizontal" role="form" name="operateEditForm" onkeydown="if(event.keyCode==13)return false;">
                  <table class="table table-condensed">
                    <th>{{languageFlag[0] == 0 ? "指令发出时间" : "Time point"}}</th>
                    <th>{{languageFlag[0] == 0 ? "指令类型" : "Cmd type"}}</th>
                    <th>{{languageFlag[0] == 0 ? "指令参数" : "Cmd param"}}</th>
                    <tr>
                      <td style="width:40%">
                        <div class="form-group">
                          <div class="col-sm-offset-1 col-sm-4">
                            <input type="text" class="form-control text-center" v-bind:value="operate.hours" required="required" name="hoursInput">
                          </div>
                          <label class="col-sm-1 control-label">:</label>
                          <div class="col-sm-4">
                            <input type="text" class="form-control text-center" v-bind:value="operate.minutes" required="required" name="minutesInput">
                          </div>
                        </div>
                      </td>
                      <td style="width:30%">
                        <div class="form-group">
                          <div class="btn-group" v-if="operate.operateType == 1">
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                              {{languageFlag[0] == 0 ? "开关" : "Switch"}}<span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu" role="menu">
                              <li><a href="javascript:void(0);" @click="changeType(2)">{{languageFlag[0] == 0 ? "调光" : "Dim"}}</a></li>
                            </ul>
                          </div>
                          <div class="btn-group" v-if="operate.operateType == 2">
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                              {{languageFlag[0] == 0 ? "调光" : "Dim"}}<span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu" role="menu">
                              <li><a href="javascript:void(0);" @click="changeType(1)">{{languageFlag[0] == 0 ? "开关" : "Switch"}}</a></li>
                            </ul>
                          </div>
                        </div>
                      </td>
                      <td style="width:30%">
                        <div class="form-group">
                          <div class="btn-group" v-if="operate.operateType == 1 && operate.operateParam == 0">
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                              OFF<span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu" role="menu">
                              <li><a href="javascript:void(0);" @click="changeParam(1)">ON</a></li>
                            </ul>
                          </div>
                          <div class="btn-group" v-if="operate.operateType == 1 && operate.operateParam == 1">
                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                              ON<span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu" role="menu">
                              <li><a href="javascript:void(0);" @click="changeParam(0)">OFF</a></li>
                            </ul>
                          </div>
                          <div class="col-sm-offset-3 col-sm-6" v-if="operate.operateType == 2">
                            <input type="text" class="form-control text-center" v-bind:value="operate.operateParam" required="required" name="operateParamInput">
                          </div>
                        </div>
                      </td>
                    </tr>
                  </table>
                </form>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">{{languageFlag[0] == 0 ? "取消" : "Cancel"}}</button>
                <button type="button" class="btn btn-primary" @click="submitEdit()">
                  {{languageFlag[0] == 0 ? "提交更改" : "Submit"}}
                </button>
              </div>
          </div><!-- /.modal-content -->
      </div><!-- /.modal -->
  </div>
  <!-- alert模态框（Modal） -->
  <div class="modal fade" id="alertM" tabindex="-1" role="dialog" aria-labelledby="alertMLabel" aria-hidden="true">
      <div class="modal-dialog">
          <div class="modal-content">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                  &times;
                </button>
                <h4 class="modal-title" id="alertMHeader">
                  {{header}}
                </h4>
              </div>
              <div class="modal-body" v-html="body">
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal" @click="buttonAction">{{languageFlag[0] == 0 ? "关闭" : "Close"}}
                </button>
              </div>
          </div><!-- /.modal-content -->
      </div><!-- /.modal -->
  </div>
  <!-- confirm模态框（Modal） -->
  <div class="modal fade" id="confirmM" tabindex="-1" role="dialog" aria-labelledby="confirmMLabel" aria-hidden="true">
      <div class="modal-dialog">
          <div class="modal-content">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                  &times;
                </button>
                <h4 class="modal-title" id="confirmMHeader">
                  {{header}}
                </h4>
              </div>
              <div class="modal-body" v-html="body">
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">{{languageFlag[0] == 0 ? "取消" : "Cancel"}}
                </button>
                <button type="button" class="btn btn-primary" @click="confirmEnter()">
                  {{languageFlag[0] == 0 ? "确认" : "OK"}}
                </button>
              </div>
          </div><!-- /.modal-content -->
      </div><!-- /.modal -->
  </div>
  <!-- promptM模态框（Modal） -->
  <div class="modal fade" id="promptM" tabindex="-1" role="dialog" aria-labelledby="promptMLabel" aria-hidden="true">
      <div class="modal-dialog">
          <div class="modal-content">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                  &times;
                </button>
                <h4 class="modal-title" id="promptMHeader">
                  {{header}}
                </h4>
              </div>
              <div class="modal-body">
                <form class="form-horizontal" role="form" name="promptForm" onkeydown="if(event.keyCode==13)return false;">
                  <div class="form-group">
                    <div class="col-sm-12">
                      <input type="text" class="form-control" placeholder="" required="required" name="promptInput">
                    </div>
                  </div>
                </form>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">{{languageFlag[0] == 0 ? "取消" : "Cancel"}}</button>
                <button type="button" class="btn btn-primary" @click="submit()">
                  {{languageFlag[0] == 0 ? "提交更改" : "Submit"}}
                </button>
              </div>
          </div><!-- /.modal-content -->
      </div><!-- /.modal -->
  </div>
  <div id="logoDiv"></div>
  <div role="navigation" id="foo" class="nav-collapse">
    <ul id="navUL">
      <li class="active">
        <a href="#devDiv" data-toggle="tab" class="navButton" id="devNav">{{languageFlag[0] == 0 ? "设备/集控器" : "Controllers"}}</a>
      </li>
      <li>
        <a href="#groupDiv" data-toggle="tab" class="navButton" id="groupNav">{{languageFlag[0] == 0 ? "分组控制" : "Groups"}}</a>
      </li>
      <li>
        <a href="#ployDiv" data-toggle="tab" class="navButton" id="ployNav">{{languageFlag[0] == 0 ? "策略控制" : "Ploys"}}</a>
      </li>
      <li>
        <a href="#userDiv" data-toggle="tab" class="navButton" id="userInfoNav">{{languageFlag[0] == 0 ? "用户信息" : "UserInfo"}}</a>
      </li>
      <li>
        <a href="#testDiv" data-toggle="tab" class="navButton" id="testNav" v-if="user.id == 100000">{{languageFlag[0] == 0 ? "指令测试" : "Console"}}</a>
      </li>
    </ul>
  </div>
  <div id="main_body" role="main" class="main mainDiv">
    <a href="#nav" class="nav-toggle">Menu</a>
    <div id="navTabContent" class="tab-content">

      <div class="tab-pane fade in active" id="devDiv"><!-- 设备 -->
        <div class="btn-group" style="float:right;">
          <button type="button" class="btn btn-default dropdown-toggle btn-language" data-toggle="dropdown">
            {{languageFlag[0] == 0 ? "Language" : "语言"}}<span class="caret"></span>
          </button>
          <ul class="dropdown-menu" role="menu">
            <li><a href="javascript:void(0);" @click="changeLanguage(0)">中文</a></li>
            <li><a href="javascript:void(0);" @click="changeLanguage(1)">English</a></li>
          </ul>
        </div>
        <div class="panel panel-info"><!-- 分块 -->
          <div class="panel-heading">
            <h3 class="panel-title">{{languageFlag[0] == 0 ? "我的设备" : "My Controller"}}</h3>
          </div>
          <div class="panel-body">
            <div class="btn-group">
              <button type="button" class="btn btn-default" @click="addDev()">{{languageFlag[0] == 0 ? "添加" : "Add"}}</button>
              <div class="btn-group">
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                  {{languageFlag[0] == 0 ? "删除" : "Delete"}}
                  <span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                  <li v-for="item of items">
                    <a href="javascript:void(0);" @click="removeDev(item)">{{item.name}}</a>
                  </li>
                </ul>
              </div>
              <div class="btn-group">
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                  {{languageFlag[0] == 0 ? "重命名" : "Rename"}}
                  <span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                  <li v-for="item of items">
                    <a href="javascript:void(0);" @click="renameDev(item)">{{item.name}}</a>
                  </li>
                </ul>
              </div>
              <!-- <div class="btn-group">
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                  跳转至详情
                  <span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                  <li v-for="item of items">
                    <a href="javascript:void(0);" v-if="item.online">{{item.name}}</a>
                  </li>
                </ul>
              </div> -->
              <!-- <button type="button" class="btn btn-default" @click="refresh()">刷新</button> -->
            </div>
          </div>
          <table class="table table-condensed">
            <th class="th-name">{{languageFlag[0] == 0 ? "名称" : "Name"}}</th>
            <th>{{languageFlag[0] == 0 ? "链接状态" : "Status"}}</th>
            <th>{{languageFlag[0] == 0 ? "在线节点" : "Nodes Online"}}</th>
            <th>{{languageFlag[0] == 0 ? "离线节点" : "Nodes Offline"}}</th>
            <th>{{languageFlag[0] == 0 ? "节点发现" : "Nodes Finder"}}</th>
            <th>{{languageFlag[0] == 0 ? "广播控制" : "Broadcast"}}</th>
            <tr v-for="item of items">
              <td class="td-name">
                <div class="btn-group">
                  <button type="button" class="btn btn-default dropdown-toggle name-button" data-toggle="dropdown">
                    {{item.name}}<span class="caret"></span>
                  </button>
                  <ul class="dropdown-menu" role="menu">
                    <li><a href="javascript:void(0);" @click="showDevDetail(item)">{{languageFlag[0] == 0 ? "详细信息" : "show detail"}}</a></li>
                    <li><a href="javascript:void(0);" @click="renameDev(item)">{{languageFlag[0] == 0 ? "修改名称" : "rename"}}</a></li>
                  </ul>
                </div>
              </td>
              <td v-if="item.online">{{languageFlag[0] == 0 ? "在线" : "Online"}}</td><td v-if="!item.online">{{languageFlag[0] == 0 ? "离线" : "Offline"}}</td>
              <td v-if="item.online">{{detail.filter(function(currentValue){return currentValue.devMac == item.mac && currentValue.online == true;}).length}}</td><td v-if="!item.online">--</td>
              <td v-if="item.online">{{detail.filter(function(currentValue){return currentValue.devMac == item.mac && currentValue.online == false;}).length}}</td><td v-if="!item.online">--</td>
              <td v-if="item.online">
                <div class="btn-group" v-if="item.zigbeeFinder == 0">
                  <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                    {{languageFlag[0] == 0 ? "打开" : "ON"}}<span class="caret"></span>
                  </button>
                  <ul class="dropdown-menu" role="menu">
                    <li><a href="javascript:void(0);" @click="turnZigbeeFinder(item, 60)">60s</a></li>
                    <li><a href="javascript:void(0);" @click="turnZigbeeFinder(item, 120)">120s</a></li>
                    <li><a href="javascript:void(0);" @click="turnZigbeeFinder(item, 240)">240s</a></li>
                  </ul>
                </div>
                <button v-if="item.zigbeeFinder == 1" type="button" class="btn btn-default" @click="turnZigbeeFinder(item, 0)">{{languageFlag[0] == 0 ? "关闭" : "OFF"}}</button>
              </td>
              <td v-if="!item.online">--</td>
              <td v-if="item.online">
                <div class="btn-group">
                  <button type="button" class="btn btn-default" @click="switchAll(item,'打开')">{{languageFlag[0] == 0 ? "打开" : "ON"}}</button>
                  <button type="button" class="btn btn-default" @click="switchAll(item,'关闭')">{{languageFlag[0] == 0 ? "关闭" : "OFF"}}</button>
                  <button type="button" class="btn btn-default" @click="setAllBrightness(item)">{{languageFlag[0] == 0 ? "调光" : "DIM"}}</button>
                </div>
              </td><td v-if="!item.online">--</td>
            </tr>
          </table>
        </div><!-- 分块 -->

        <div class="panel panel-info" v-for="item of items"><!-- 分块 -->
          <div class="panel-heading">
            <h3 class="panel-title">{{item.name}}</h3>
          </div>
          <div class="panel-body">
            <p v-if="item.online">{{languageFlag[0] == 0 ? "在线节点" : "Nodes Online"}}：{{detail.filter(function(currentValue){return currentValue.devMac == item.mac && currentValue.online == true;}).length}} {{languageFlag[0] == 0 ? "离线节点" : "Nodes Offline"}}：{{detail.filter(function(currentValue){return currentValue.devMac == item.mac && currentValue.online == false;}).length}}</p>
            <p v-if="!item.online">{{languageFlag[0] == 0 ? "在线节点" : "Nodes Online"}}：-- {{languageFlag[0] == 0 ? "离线节点" : "Nodes Offline"}}：--</p>
          </div>
          <table class="table table-condensed">
            <th class="th-name">{{languageFlag[0] == 0 ? "节点名称" : "Node Name"}}</th>
            <th>{{languageFlag[0] == 0 ? "网络" : "Network"}}</th>
            <th>{{languageFlag[0] == 0 ? "亮度" : "Brightness"}}</th>
            <th>{{languageFlag[0] == 0 ? "状态" : "Status"}}</th>
            <th>
              <div class="btn-group">
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                  {{languageFlag[0] == 0 ? "温度" : "Temperature"}}<span class="caret"></span>
                </button>
                <ul class="dropdown-menu" role="menu">
                  <li><a href="javascript:void(0);" @click="temperatureTransform(false)">摄氏(&#176;C)</a></li>
                  <li><a href="javascript:void(0);" @click="temperatureTransform(true)">华氏(&#176;F)</a></li>
                </ul>
              </div>
            </th>
            <th>{{languageFlag[0] == 0 ? "湿度" : "Humidity"}}</th>
            <th>
              <div class="btn-group">
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                  {{languageFlag[0] == 0 ? "节点控制" : "Control"}}<span class="caret"></span>
                </button>
                <ul class="dropdown-menu" role="menu">
                  <li><a href="javascript:void(0);" @click="switchAll(item,'打开')">{{languageFlag[0] == 0 ? "广播打开" : "Broadcast ON"}}</a></li>
                  <li><a href="javascript:void(0);" @click="switchAll(item,'关闭')">{{languageFlag[0] == 0 ? "广播关闭" : "Broadcast OFF"}}</a></li>
                  <li><a href="javascript:void(0);" @click="setAllBrightness(item)">{{languageFlag[0] == 0 ? "广播调光" : "Broadcast DIM"}}</a></li>
                </ul>
              </div>
            </th>
            <tr v-for="zb of detail" v-if="zb.devMac == item.mac && zb.online == true">
              <td class="td-name">
                <div class="btn-group">
                  <button type="button" class="btn btn-default dropdown-toggle name-button" data-toggle="dropdown">
                    {{zb.name}}<span class="caret"></span>
                  </button>
                  <ul class="dropdown-menu" role="menu">
                    <li><a href="javascript:void(0);" @click="showZigbeeDetail(zb)">{{languageFlag[0] == 0 ? "详细信息" : "show detail"}}</a></li>
                    <li><a href="javascript:void(0);" @click="renameZigbee(zb)">{{languageFlag[0] == 0 ? "修改名称" : "rename"}}</a></li>
                    <li v-if="!zb.online"><a href="javascript:void(0);" @click="removeOfflineZigbee(zb)">{{languageFlag[0] == 0 ? "删除节点" : "delete"}}</a></li>
                  </ul>
                </div>
              </td>
              <td v-if="zb.online">{{languageFlag[0] == 0 ? "在线" : "Online"}}</td><td v-if="!zb.online">{{languageFlag[0] == 0 ? "离线" : "Offline"}}</td>
              <td v-if="zb.online">{{zb.brightness}}</td><td v-if="!zb.online">--</td>
              <td v-if="zb.online && zb.workStatus">
                ON
              </td>
              <td v-if="zb.online && !zb.workStatus">
                OFF
              </td>
              <td v-if="!zb.online">--</td>
              <td v-if="zb.type == 1 || zb.type == 2 || zb.type == 3">N</td>
              <td v-if="!temperatureF && (zb.type != 1 && zb.type != 2 && zb.type != 3)">{{(zb.temperature != null && zb.temperature != undefined) ? zb.temperature : "N"}}&#176;C</td>
              <td v-if="temperatureF && (zb.type != 1 && zb.type != 2 && zb.type != 3)">{{(zb.temperature != null && zb.temperature != undefined) ? zb.temperature * 1.8 + 32 : "N"}}&#176;F</td>
              <td v-if="zb.type == 1 || zb.type == 2 || zb.type == 3">N</td>
              <td v-if="!(zb.type == 1 || zb.type == 2 || zb.type == 3)">{{zb.humidity}}</td>
              <td>
                <div class="btn-group" v-if="zb.online">
                  <!-- <button v-if="!zb.workStatus" type="button" class="btn btn-default" @click="switchSingle(zb,'打开')">打开</button>
                  <button v-if="zb.workStatus" type="button" class="btn btn-default" @click="switchSingle(zb,'关闭')">关闭</button> -->
                  <button type="button" class="btn btn-default" @click="switchSingle(zb,'打开')">{{languageFlag[0] == 0 ? "打开" : "ON"}}</button>
                  <button type="button" class="btn btn-default" @click="switchSingle(zb,'关闭')">{{languageFlag[0] == 0 ? "关闭" : "OFF"}}</button>
                  <button type="button" class="btn btn-default" @click="setBrightness(zb)">{{languageFlag[0] == 0 ? "调光" : "DIM"}}</button>
                </div>
                <p v-if="!zb.online">--</p>
              </td>
            </tr>
             <tr v-for="zb of detail" v-if="zb.devMac == item.mac && zb.online == false">
              <td>
                <div class="btn-group">
                  <button type="button" class="btn btn-default dropdown-toggle name-button" data-toggle="dropdown">
                    {{zb.name}}<span class="caret"></span>
                  </button>
                  <ul class="dropdown-menu" role="menu">
                    <li><a href="javascript:void(0);" @click="showZigbeeDetail(zb)">{{languageFlag[0] == 0 ? "详细信息" : "show detail"}}</a></li>
                    <li><a href="javascript:void(0);" @click="renameZigbee(zb)">{{languageFlag[0] == 0 ? "修改名称" : "rename"}}</a></li>
                    <li v-if="!zb.online"><a href="javascript:void(0);" @click="removeOfflineZigbee(zb)">{{languageFlag[0] == 0 ? "删除节点" : "delete"}}</a></li>
                  </ul>
                </div>
              </td>
              <td v-if="zb.online">{{languageFlag[0] == 0 ? "在线" : "Online"}}</td><td v-if="!zb.online">{{languageFlag[0] == 0 ? "离线" : "Offline"}}</td>
              <td v-if="zb.online">{{zb.brightness}}</td><td v-if="!zb.online">--</td>
              <td v-if="zb.online && zb.workStatus">
                ON
              </td>
              <td v-if="zb.online && !zb.workStatus">
                OFF
              </td>
              <td v-if="!zb.online">--</td>
              <td>--</td>
              <td>--</td>
              <td v-if="zb.online">
                <div class="btn-group">
                  <button type="button" class="btn btn-default" @click="switchSingle(zb,'打开')">{{languageFlag[0] == 0 ? "打开" : "ON"}}</button>
                  <button type="button" class="btn btn-default" @click="switchSingle(zb,'关闭')">{{languageFlag[0] == 0 ? "关闭" : "OFF"}}</button>
                  <button type="button" class="btn btn-default" @click="setBrightness(zb)">{{languageFlag[0] == 0 ? "调光" : "DIM"}}</button>
                </div>
              </td><td v-if="!zb.online">--</td>
            </tr>
          </table>
        </div><!-- 分块 -->
      </div><!-- 首页 -->
      <div class="tab-pane fade" id="groupDiv"><!-- 分组 -->
        <div class="panel panel-info"><!-- 分块 -->
          <div class="panel-heading">
            <h3 class="panel-title">{{languageFlag[0] == 0 ? "我的分组" : "My Groups"}}</h3>
          </div>
          <div class="panel-body">
            <div class="btn-group">
              <button type="button" class="btn btn-default" @click="addGroup()">{{languageFlag[0] == 0 ? "添加分组" : "Add Group"}}</button>
              <div class="btn-group">
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                  {{languageFlag[0] == 0 ? "删除分组" : "Delete Group"}}
                  <span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                  <li v-for="item of items"><a href="javascript:void(0);" @click="removeGroup(item)">{{item.name}}</a></li>
                </ul>
              </div>
              <div class="btn-group">
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                  {{languageFlag[0] == 0 ? "组重命名" : "Rename Group"}}
                  <span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                  <li v-for="item of items"><a href="javascript:void(0);" @click="renameGroup(item)">{{item.name}}</a></li>
                </ul>
              </div>
              <!-- <button type="button" class="btn btn-default" @click="refresh()">刷新</button> -->
            </div>
          </div>
          <table class="table table-condensed">
            <th>{{languageFlag[0] == 0 ? "组名称" : "Group Name"}}</th>
            <th>{{languageFlag[0] == 0 ? "在线节点" : "Nodes Online"}}</th>
            <th>{{languageFlag[0] == 0 ? "离线节点" : "Nodes Offline"}}</th>
            <th>{{languageFlag[0] == 0 ? "整组控制" : "Group Control"}}</th>
            <tr v-for="item of items">
              <td class="td-name">
                <div class="btn-group">
                  <button type="button" class="btn btn-default dropdown-toggle name-button" data-toggle="dropdown">
                    {{item.name}}<span class="caret"></span>
                  </button>
                  <ul class="dropdown-menu" role="menu">
                    <li><a href="javascript:void(0);" @click="showGroupDetail(item)">{{languageFlag[0] == 0 ? "详细信息" : "show detail"}}</a></li>
                    <li><a href="javascript:void(0);" @click="renameGroup(item)">{{languageFlag[0] == 0 ? "修改名称" : "rename"}}</a></li>
                  </ul>
                </div>
              </td>
              <td>{{detail.filter(function(currentValue){return currentValue.groupAddrs.indexOf(item.addr) != -1 && currentValue.online == true;}).length}}</td>
              <td>{{detail.filter(function(currentValue){return currentValue.groupAddrs.indexOf(item.addr) != -1 && currentValue.online == false;}).length}}</td>
              <td>
                <div class="btn-group">
                  <button type="button" class="btn btn-default" @click="switchByGroup(item,'打开')">{{languageFlag[0] == 0 ? "打开" : "ON"}}</button>
                  <button type="button" class="btn btn-default" @click="switchByGroup(item,'关闭')">{{languageFlag[0] == 0 ? "关闭" : "OFF"}}</button>
                  <button type="button" class="btn btn-default" @click="setBrightnessByGroup(item)">{{languageFlag[0] == 0 ? "调光" : "DIM"}}</button>
                </div>
              </td>
            </tr>
          </table>
        </div><!-- 分块 -->
        <div class="panel panel-info" v-for="item of items"><!-- 分块 -->
          <div class="panel-heading">
            <h3 class="panel-title">{{item.name}}</h3>
          </div>
          <div class="panel-body">
            <p>{{languageFlag[0] == 0 ? "在线节点" : "Nodes Online"}}：{{detail.filter(function(currentValue){return currentValue.groupAddrs.indexOf(item.addr) != -1 && currentValue.online == true;}).length}} {{languageFlag[0] == 0 ? "离线节点" : "Nodes Offline"}}：{{detail.filter(function(currentValue){return currentValue.groupAddrs.indexOf(item.addr) != -1 && currentValue.online == false;}).length}}</p>
            <div class="btn-group">
              <!-- <button type="button" class="btn btn-default">添加节点</button> -->
              <div class="btn-group">
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="modal" data-target="#addToGroupM" @click="addZigbeeToGroup(item)">
                  {{languageFlag[0] == 0 ? "添加节点" : "Add Nodes"}}
                </button>
              </div>
              <div class="btn-group">
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                  {{languageFlag[0] == 0 ? "删除节点" : "Delete Node"}}
                  <span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                  <li v-for="zb of detail" v-if="zb.groupAddrs.indexOf(item.addr) != -1 && zb.online == true"><a href="javascript:void(0);" @click="removeZigbeeFromGroup(zb,item)">{{zb.name}}</a></li>
                </ul>
              </div>
              <button type="button" class="btn btn-default" @click="renameGroup(item)">{{languageFlag[0] == 0 ? "修改组名" : "Rename Group"}}</button>
            </div>
          </div>
          <table class="table table-condensed">
            <th class="th-name">{{languageFlag[0] == 0 ? "节点名称" : "Node Name"}}</th>
            <th>{{languageFlag[0] == 0 ? "网络" : "Network"}}</th>
            <th>{{languageFlag[0] == 0 ? "亮度" : "Brightness"}}</th>
            <th>{{languageFlag[0] == 0 ? "状态" : "Status"}}</th>
            <th>
              <div class="btn-group">
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                  {{languageFlag[0] == 0 ? "温度" : "Temperature"}}<span class="caret"></span>
                </button>
                <ul class="dropdown-menu" role="menu">
                  <li><a href="javascript:void(0);" @click="temperatureTransform(false)">摄氏(&#176;C)</a></li>
                  <li><a href="javascript:void(0);" @click="temperatureTransform(true)">华氏(&#176;F)</a></li>
                </ul>
              </div>
            </th>
            <th>{{languageFlag[0] == 0 ? "湿度" : "Humidity"}}</th>
            <th>
              <div class="btn-group">
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                  {{languageFlag[0] == 0 ? "节点控制" : "Control"}}<span class="caret"></span>
                </button>
                <ul class="dropdown-menu" role="menu">
                  <li><a href="javascript:void(0);" @click="switchByGroup(item,'打开')">{{languageFlag[0] == 0 ? "整组打开" : "Broadcast ON"}}</a></li>
                  <li><a href="javascript:void(0);" @click="switchByGroup(item,'关闭')">{{languageFlag[0] == 0 ? "整组关闭" : "Broadcast OFF"}}</a></li>
                  <li><a href="javascript:void(0);" @click="setBrightnessByGroup(item)">{{languageFlag[0] == 0 ? "整组调光" : "Broadcast DIM"}}</a></li>
                </ul>
              </div>
            </th>
            <tr v-for="zb of detail" v-if="zb.groupAddrs.indexOf(item.addr) != -1 && zb.online == true">
              <td class="td-name">
                <div class="btn-group">
                  <button type="button" class="btn btn-default dropdown-toggle name-button" data-toggle="dropdown">
                    {{zb.name}}<span class="caret"></span>
                  </button>
                  <ul class="dropdown-menu" role="menu">
                    <li><a href="javascript:void(0);" @click="showZigbeeDetail(zb)">{{languageFlag[0] == 0 ? "详细信息" : "show detail"}}</a></li>
                    <!-- <li><a href="javascript:void(0);">修改名称</a></li> -->
                  </ul>
                </div>
              </td>
              <td v-if="zb.online">{{languageFlag[0] == 0 ? "在线" : "Online"}}</td><td v-if="!zb.online">{{languageFlag[0] == 0 ? "离线" : "Offline"}}</td>
              <td v-if="zb.online">{{zb.brightness}}</td><td v-if="!zb.online">--</td>
              <td v-if="zb.online && zb.workStatus">
                ON
              </td>
              <td v-if="zb.online && !zb.workStatus">
                OFF
              </td>
              <td v-if="!zb.online">--</td>
              <td v-if="zb.type == 1 || zb.type == 2 || zb.type == 3">N</td>
              <td v-if="!temperatureF && (zb.type != 1 && zb.type != 2 && zb.type != 3)">{{(zb.temperature != null && zb.temperature != undefined) ? zb.temperature : "N"}}&#176;C</td>
              <td v-if="temperatureF && (zb.type != 1 && zb.type != 2 && zb.type != 3)">{{(zb.temperature != null && zb.temperature != undefined) ? zb.temperature * 1.8 + 32 : "N"}}&#176;F</td>
              <td v-if="zb.type == 1 || zb.type == 2 || zb.type == 3">N</td>
              <td v-if="!(zb.type == 1 || zb.type == 2 || zb.type == 3)">{{zb.humidity}}</td>
              <td>
                <div class="btn-group" v-if="zb.online">
                  <button type="button" class="btn btn-default" @click="switchSingle(zb,'打开')">{{languageFlag[0] == 0 ? "打开" : "ON"}}</button>
                  <button type="button" class="btn btn-default" @click="switchSingle(zb,'关闭')">{{languageFlag[0] == 0 ? "关闭" : "OFF"}}</button>
                  <button type="button" class="btn btn-default" @click="setBrightness(zb)">{{languageFlag[0] == 0 ? "调光" : "DIM"}}</button>
                </div>
                <p v-if="!zb.online">--</p>
              </td>
            </tr>
            <tr v-for="zb of detail" v-if="zb.groupAddrs.indexOf(item.addr) != -1 && zb.online == false">
              <td>
                <div class="btn-group">
                  <button type="button" class="btn btn-default dropdown-toggle name-button" data-toggle="dropdown">
                    {{zb.name}}<span class="caret"></span>
                  </button>
                  <ul class="dropdown-menu" role="menu">
                    <li><a href="javascript:void(0);" @click="showZigbeeDetail(zb)">{{languageFlag[0] == 0 ? "详细信息" : "show detail"}}</a></li>
                    <!-- <li><a href="javascript:void(0);">修改名称</a></li> -->
                  </ul>
                </div>
              </td>
              <td v-if="zb.online">{{languageFlag[0] == 0 ? "在线" : "Online"}}</td><td v-if="!zb.online">{{languageFlag[0] == 0 ? "离线" : "Offline"}}</td>
              <td v-if="zb.online">{{zb.brightness}}</td><td v-if="!zb.online">--</td>
              <td v-if="zb.online && zb.workStatus">
                ON
              </td>
              <td v-if="zb.online && !zb.workStatus">
                OFF
              </td>
              <td v-if="!zb.online">--</td>
              <td>--</td>
              <td>--</td>
              <td>
                <div class="btn-group" v-if="zb.online">
                  <button type="button" class="btn btn-default" @click="switchSingle(zb,'打开')">{{languageFlag[0] == 0 ? "打开" : "ON"}}</button>
                  <button type="button" class="btn btn-default" @click="switchSingle(zb,'关闭')">{{languageFlag[0] == 0 ? "关闭" : "OFF"}}</button>
                  <button type="button" class="btn btn-default" @click="setBrightness(zb)">{{languageFlag[0] == 0 ? "调光" : "DIM"}}</button>
                </div>
                <p v-if="!zb.online">--</p>
              </td>
            </tr>
          </table>
        </div><!-- 分块 -->
      </div><!-- 首页 -->
      <div class="tab-pane fade" id="ployDiv"><!-- 策略 -->
        <div class="panel panel-info"><!-- 分块 -->
          <div class="panel-heading">
            <h3 class="panel-title">{{languageFlag[0] == 0 ? "我的策略" : "My Ploys"}}</h3>
          </div>
          <div class="panel-body">
            <div class="btn-group">
              <button type="button" class="btn btn-default" @click="addPloy()">{{languageFlag[0] == 0 ? "添加策略" : "Add Ploy"}}</button>
              <div class="btn-group">
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                  {{languageFlag[0] == 0 ? "删除策略" : "Delete Ploy"}}
                  <span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                  <li v-for="item of items"><a href="javascript:void(0);" @click="removePloy(item)">{{item.name}}</a></li>
                </ul>
              </div>
              <div class="btn-group">
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                  {{languageFlag[0] == 0 ? "策略重命名" : "Rename"}}
                  <span class="caret"></span>
                </button>
                <ul class="dropdown-menu">
                  <li v-for="item of items"><a href="javascript:void(0);" @click="renamePloy(item)">{{item.name}}</a></li>
                </ul>
              </div>
              <button type="button" class="btn btn-default" @click="refresh()">{{languageFlag[0] == 0 ? "刷新" : "Refresh"}}</button>
            </div>
          </div>
          <table class="table table-condensed">
            <th class="th-name">{{languageFlag[0] == 0 ? "策略名称" : "Ploy Name"}}</th>
            <th>{{languageFlag[0] == 0 ? "下一时刻" : "NextTimePoint"}}</th>
            <th>{{languageFlag[0] == 0 ? "下一指令" : "NextCmd"}}</th>
            <th>{{languageFlag[0] == 0 ? "执行状态" : "Status"}}</th>
            <th>{{languageFlag[0] == 0 ? "策略控制" : "Ploy Control"}}</th>
            <tr v-for="item of items">
              <td class="td-name">
                <div class="btn-group">
                  <button type="button" class="btn btn-default dropdown-toggle name-button" data-toggle="dropdown">
                    {{item.name}}<span class="caret"></span>
                  </button>
                  <ul class="dropdown-menu" role="menu">
                    <li><a href="javascript:void(0);" @click="showPloyDetail(item)">{{languageFlag[0] == 0 ? "详细信息" : "show detail"}}</a></li>
                    <li><a href="javascript:void(0);" @click="renamePloy(item)">{{languageFlag[0] == 0 ? "修改名称" : "rename"}}</a></li>
                  </ul>
                </div>
              </td>
              <td v-if="item.status == 1">
                {{item.currentOperate.hours < 10 ? "0" + item.currentOperate.hours : item.currentOperate.hours}}
                :
                {{item.currentOperate.minutes < 10 ? "0" + item.currentOperate.minutes : item.currentOperate.minutes}}
              </td>
              <td v-if="!item.status == 1">--</td>
              <td v-if="item.status == 1 && item.currentOperate.operateType == 1 && item.currentOperate.operateParam == 1">
                ON
              </td>
              <td v-if="item.status == 1 && item.currentOperate.operateType == 1 && item.currentOperate.operateParam == 0">
                OFF
              </td>
              <td v-if="item.status == 1 && item.currentOperate.operateType == 2">
                {{languageFlag[0] == 0 ? "调光" : "Dim to"}}:{{item.currentOperate.operateParam}}
              </td>
              <td v-if="!item.status == 1">--</td>
              <td v-if="item.status == 1">{{languageFlag[0] == 0 ? "正在执行" : "Running"}}</td><td v-if="!item.status == 1">{{languageFlag[0] == 0 ? "未执行" : "Stopped"}}</td>
              <td>
                <div class="btn-group">
                  <button type="button" class="btn btn-default" v-if="!item.status == 1" @click="start(item)">{{languageFlag[0] == 0 ? "开始执行" : "Run"}}</button>
                  <button type="button" class="btn btn-default" v-if="item.status == 1" @click="stop(item)">{{languageFlag[0] == 0 ? "停止执行" : "Stop"}}</button>
                </div>
              </td>
            </tr>
          </table>
        </div><!-- 分块 -->
        <div class="panel panel-info" v-for="item of items"><!-- 分块 -->
          <div class="panel-heading">
            <h3 class="panel-title">{{item.name}} -- {{item.groupName}}</h3>
          </div>
          <div class="panel-body">
            <p></p>
            <div class="btn-group">
              <button type="button" class="btn btn-default" v-if="!item.status == 1" @click="start(item)">{{languageFlag[0] == 0 ? "开始执行" : "Run"}}</button>
              <button type="button" class="btn btn-default" v-if="item.status == 1" @click="stop(item)">{{languageFlag[0] == 0 ? "停止执行" : "Stop"}}</button>
              <button type="button" class="btn btn-default" @click="changeBind(item)">{{languageFlag[0] == 0 ? "更改分组" : "ChangeGroup"}}</button>
              <button type="button" class="btn btn-default" v-if="!item.editStarted" @click="startEdit(item)">{{languageFlag[0] == 0 ? "开始编辑" : "Edit"}}</button>
              <button type="button" class="btn btn-default" v-if="item.editStarted" @click="saveEdit(item)">{{languageFlag[0] == 0 ? "保存编辑" : "Save"}}</button>
              <button type="button" class="btn btn-default" v-if="item.editStarted" @click="cancelEdit(item)">{{languageFlag[0] == 0 ? "取消编辑" : "Cancel"}}</button>
            </div>
            <div class="btn-group pull-right">
              <button type="button" class="btn btn-default" @click="">{{languageFlag[0] == 0 ? "时区" : "Time zone"}}:{{new Date().toTimeString().substring(8)}}</button>
            </div>
          </div>
          <table class="table table-condensed">
            <th>{{languageFlag[0] == 0 ? "指令发出时间" : "Time Point"}}</th>
            <th>{{languageFlag[0] == 0 ? "指令类型" : "Cmd Type"}}</th>
            <th>{{languageFlag[0] == 0 ? "指令参数" : "Cmd param"}}</th>
            <th v-if="item.editStarted">
              <div class="btn-group">
                  <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                    {{languageFlag[0] == 0 ? "编辑" : "Edit"}}<span class="caret"></span>
                  </button>
                  <ul class="dropdown-menu" role="menu">
                    <li><a href="javascript:void(0);" @click="removeAllOperate(item)">{{languageFlag[0] == 0 ? "删除全部" : "Delete All"}}</a></li>
                  </ul>
                </div>
            </th>
            <tr v-for="operate of item.operateArray">
              <td>
                {{operate.hours < 10 ? "0" + operate.hours : operate.hours}}
                :
                {{operate.minutes < 10 ? "0" + operate.minutes : operate.minutes}}
              </td>
              <td v-if="operate.operateType == 1">
                {{languageFlag[0] == 0 ? "开关" : "Switch"}}
              </td>
              <td v-if="operate.operateType == 2">
                {{languageFlag[0] == 0 ? "调光" : "Dim"}}
              </td>
              <td v-if="operate.operateType == 1 && operate.operateParam == 1">
                ON
              </td>
              <td v-if="operate.operateType == 1 && operate.operateParam == 0">
                OFF
              </td>
              <td v-if="operate.operateType == 2">
                {{operate.operateParam}}
              </td>
              <td v-if="item.editStarted">
                <button type="button" class="btn btn-default" @click="editOperate(item, operate)">{{languageFlag[0] == 0 ? "修改" : "Edit"}}</button>
                <button type="button" class="btn btn-default" @click="removeOperate(item,operate)">{{languageFlag[0] == 0 ? "删除" : "Delete"}}</button>
              </td>
            </tr>
            <tr v-if="item.editStarted">
              <td></td><td></td><td></td>
              <td>
                <button type="button" class="btn btn-default" @click="addOperate(item)">+</button>
              </td>
            </tr>
          </table>
        </div><!-- 分块 -->
      </div><!-- 首页 -->
      <div class="tab-pane fade" id="userDiv"><!-- 用户 -->

        <div><!-- 分块 -->
          <form class="form-horizontal" role="form" name="userInfoform" onkeydown="if(event.keyCode==13)return false;">
            <div class="form-group">
              <label class="col-sm-2 control-label">{{languageFlag[0] == 0 ? "用户名" : "Username"}}</label>
              <div class="col-sm-10">
                <p class="form-control-static">{{item.username}}</p>
              </div>
            </div>
            <div class="form-group">
              <label class="col-sm-2 control-label">{{languageFlag[0] == 0 ? "邮箱" : "Email"}}</label>
              <div class="col-sm-10">
                <p class="form-control-static">{{item.email}}</p>
              </div>
            </div>
            <div class="form-group">
              <label class="col-sm-2 control-label">{{languageFlag[0] == 0 ? "电话" : "Phone"}}</label>
              <div class="col-sm-10">
                <p class="form-control-static">{{item.phone}}</p>
              </div>
            </div>
            <div class="form-group" v-if="!flags.hidden">
              <label class="col-sm-2 control-label">{{languageFlag[0] == 0 ? "旧密码" : "Password"}}</label>
              <div class="col-sm-4">
                <input type="password" class="form-control" placeholder="password" required="required" name="oldPassword">
              </div>
            </div>
            <div class="form-group" v-if="!flags.hidden">
              <label class="col-sm-2 control-label">{{languageFlag[0] == 0 ? "新密码" : "NewPassword"}}</label>
              <div class="col-sm-4">
                <input type="password" class="form-control" placeholder="newpassword" required="required" name="newPassword" onblur="passwordTest()" onkeyup="passwordTest()">
              </div>
            </div>
            <div class="form-group" v-if="!flags.hidden">
              <label class="col-sm-2 control-label">{{languageFlag[0] == 0 ? "确认新密码" : "RepeatNewPassword"}}</label>
              <div class="col-sm-4">
                <input type="password" class="form-control" placeholder="newpassword" required="required" name="newPasswordRepeat" onblur="passwordTest()" onkeyup="passwordTest()">
                <span style="color:red; display:none" id="passwordTestSpan">{{languageFlag[0] == 0 ? "两次输入密码不一致" : "Those passwords didn't match. Try again."}}</span>
              </div>
            </div>
            <div class="form-group">
              <div class="col-sm-2">
                <button v-if="!flags.hidden" type="button" class="btn btn-default form-control" @click="changePasswordEnter()">{{languageFlag[0] == 0 ? "确认" : "Confirm"}}</button>
              </div>
              <div class="col-sm-2">
                <button type="button" class="btn btn-default form-control" @click="changePassword()">{{flags.buttonText}}</button>
              </div>
            </div>
          </form>
        </div><!-- 分块 -->
        
      </div><!-- 首页 -->
      <div class="tab-pane fade" id="testDiv"><!-- 测试 -->

        <div><!-- 分块 -->
          <form class="form-horizontal" role="form" name="socketForm" onkeydown="if(event.keyCode==13)return false;">
            <div class="form-group">
              <label class="col-sm-2 control-label">socket广播</label>
              <div class="col-sm-4">
                <input type="text" class="form-control" placeholder="command" required="required" name="commandInput">
              </div>
              <div class="col-sm-2">
                <button type="button" class="btn btn-default form-control" @click="sendCmdToAllSocket()">发送指令</button>
              </div>
            </div>
            <div class="form-group">
              <p v-if="!socketFlag.hidden" class="col-sm-offset-7">已发送</p>
            </div>
            <div class="form-group">
              <div col-sm-4>
                <button type="button" class="btn btn-default form-control" @click="turnHeartBeat()">开启/关闭心跳包回复</button>
              </div>
            </div>
          </form>
        </div><!-- 分块 -->
        
      </div><!-- 首页 -->
    </div>
  </div>

  <script>
    var navigation = responsiveNav("foo", {customToggle: ".nav-toggle"});
  </script>
</div>
</body>
<!-- <script type="text/javascript" src="index.js"></script> -->
<script type="text/javascript">

// var localhost = "114.115.155.187";
// var localhost = "localhost";
var localhost = "<%=request.getContextPath()%>";
// var languageFlag = 1;

/*********************对象构造方法*********************/
function Dev(devName, macAddr, devNet) {//集控器
	this.name = devName;
	this.mac = macAddr;
  if (devNet == 1) {
    this.online = true;
  } else {
    this.online = false;
  }
}

function Zigbee(zigbeeName, mac, devMac, zigbeeBright, zigbeeNet, zigbeeStatus,
  version, type, temperature, humidity, minPower) {//zigbee节点
	this.name = zigbeeName;
  if (zigbeeNet == 1) {
    this.online = true;
  } else {
    this.online = false;
  }
	this.brightness = zigbeeBright;
  if (zigbeeStatus == 1) {
    this.workStatus = true;
  } else {
    this.workStatus = false;
  }
	this.mac = mac;
	this.devMac = devMac;
	this.groupAddrs = new Array();
  this.groupNames = new Array();
  this.version = version;
  this.type = type;
  this.temperature = temperature;
  this.humidity = humidity;
  this.minPower = minPower;
}
function Group(groupName, groupAddr) {//控制组
	this.name = groupName;
	this.addr = groupAddr;
	this.zigbeeOnlineSet = new Array();
	this.zigbeeOfflineSet = new Array();
}
function PloyOperate(id, hours, minutes, operateType, operateParam, ployid) {//策略项
  this.id = id;
	this.hours = hours;
	this.minutes = minutes;
	this.operateType = operateType;//操作类型(1,开关、2,调光)
	this.operateParam = operateParam;//参数(1开，0关)
  this.ployid = ployid;
}
function Ploy(ployName, ployId, status, userid, bindType, bindParam, timeZone) {//控制策略
	this.name = ployName;//策略名
	this.id = ployId;//策略id
	this.status = status;//是否正在运行
  this.userid = userid;//用户id
  this.bindType = bindType;//绑定类型(1组、2集控器、3节点)
  this.bindParam = bindParam;//绑定参数(组id、集控器mac地址、节点mac地址)
  this.timeZone = timeZone;//时区(时间差值，单位分钟)
	this.currentOperate = new PloyOperate(0, 0, 0, 1, 0);
	this.operateArray = new Array();
  this.operateBackUp;
  this.editStarted = false;
}
function User() {//用户数据
	this.username = String();
	this.password = String();
	this.email = String();
  // this.id = String();
	this.devSet = new Array();
	this.groupSet = new Array();
	this.zigbeeSet = new Array();
	this.ployArray = new Array();
  this.languageFlag = new Array();
  this.languageFlag[0] = 1;
}
/*********************对象构造方法*********************/

//数据处理方法
function userDataRefresh(user, jsonObj) {
  var count;
  user.id = jsonObj.user.id;
  user.username = jsonObj.user.username;
  user.password = jsonObj.user.password;
  user.email = jsonObj.user.email;
  user.phone = jsonObj.user.phone;

  user.devSet.splice(0,user.devSet.length);
  user.groupSet.splice(0,user.groupSet.length);
  user.zigbeeSet.splice(0,user.zigbeeSet.length);
  // user.ployArray.splice(0,user.ployArray.length);

  for (var i in jsonObj.devArr) {
    user.devSet.push(new Dev(jsonObj.devArr[i].devName, jsonObj.devArr[i].devMac, jsonObj.devArr[i].devNet));
    for (var index in jsonObj.devAttrArr) {
      if (jsonObj.devAttrArr[index].devMac == jsonObj.devArr[i].devMac) {
        count = user.devSet.length - 1;
        user.devSet[count].zigbeeFinder = jsonObj.devAttrArr[index].zigbeeFinder == null || undefined ? 0 : jsonObj.devAttrArr[index].zigbeeFinder;
      }
    }
  }

  for (var i in jsonObj.groupArr) {
    user.groupSet.push(new Group(jsonObj.groupArr[i].groupName, jsonObj.groupArr[i].groupid));
  }

  for (var i in jsonObj.zigbeeArr) {
    user.zigbeeSet.push(new Zigbee(
      jsonObj.zigbeeArr[i].zigbeeName, 
      jsonObj.zigbeeArr[i].zigbeeMac, 
      jsonObj.zigbeeArr[i].devMac, 
      jsonObj.zigbeeArr[i].zigbeeBright, 
      jsonObj.zigbeeArr[i].zigbeeNet, 
      jsonObj.zigbeeArr[i].zigbeeStatus
      ));
    for (var index in jsonObj.zigbeeAttrArr) {
      if (jsonObj.zigbeeAttrArr[index].zigbeeMac == jsonObj.zigbeeArr[i].zigbeeMac) {
        count = user.zigbeeSet.length - 1;
        user.zigbeeSet[count].temperature = jsonObj.zigbeeAttrArr[index].temperature / 100;
        user.zigbeeSet[count].humidity = jsonObj.zigbeeAttrArr[index].humidity / 100;
        user.zigbeeSet[count].type = jsonObj.zigbeeAttrArr[index].type;
        if (user.zigbeeSet[count].type != null) {
          if (user.zigbeeSet[count].type == 1 || user.zigbeeSet[count].type == 2 || user.zigbeeSet[count].type == 17 || user.zigbeeSet[count].type == 18) {
            user.zigbeeSet[count].minPower = 50;
          } else if (user.zigbeeSet[count].type == 3 || user.zigbeeSet[count].type == 19) {
            user.zigbeeSet[count].minPower = 1;
          } else {
            user.zigbeeSet[count].minPower = 1;
          }
        } else {
          user.zigbeeSet[count].minPower = 1;
        }
        user.zigbeeSet[count].power = jsonObj.zigbeeAttrArr[index].power;
      }
    }
  }

  for (var i in jsonObj.groupPairArr) {
    for (var j in user.zigbeeSet) {
      if (user.zigbeeSet[j].mac == jsonObj.groupPairArr[i].zigbeeMac) {
        user.zigbeeSet[j].groupAddrs.push(jsonObj.groupPairArr[i].groupid);
        for (var x in user.groupSet) {
          if (user.groupSet[x].addr == jsonObj.groupPairArr[i].groupid) {
            user.zigbeeSet[j].groupNames.push(user.groupSet[x].name);
          }
        }
      }
    }
  }
  
}

function getLocalTime(hours, minutes, offset) {
  var hoursOffset = offset / 60;
  var minutesOffset = offset % 60;
  var localhours = hours - hoursOffset;
  var localminutes = minutes - minutesOffset;
  if (localminutes < 0) {
    localminutes += 60;
    localhours -= 1;
  } else if (localminutes >= 60) {
    localminutes -= 60;
    localhours += 1;
  }
  localhours = localhours % 24;
  if (localhours < 0) {
    localhours += 24;
  }
  return {
    "localhours":localhours,
    "localminutes":localminutes
  };
}

function getGMTTime(localhours, localminutes, offset) {
  var hoursOffset = offset / 60;
  var minutesOffset = offset % 60;
  var gmthours = parseInt(localhours,10) + hoursOffset;
  var gmtminutes = parseInt(localminutes,10) + minutesOffset;
  if (gmtminutes < 0) {
    gmtminutes += 60;
    gmthours -= 1;
  } else if (gmtminutes >= 60) {
    gmtminutes -= 60;
    gmthours += 1;
  }
  gmthours = gmthours % 24;
  if (gmthours < 0) {
    gmthours += 24;
  }
  return {
    "gmthours":gmthours,
    "gmtminutes":gmtminutes
  };
}

function ployDataRefresh(user, jsonObj) {
  user.ployArray.splice(0,user.ployArray.length);
  var timeZoneOffset = new Date().getTimezoneOffset();
  var currentHours = new Date().getHours();
  var currentMinutes = new Date().getMinutes();
  var localtime;
  for (var i in jsonObj.ployArr) {
    user.ployArray.push(new Ploy(
      jsonObj.ployArr[i].ployName,
      jsonObj.ployArr[i].id,
      jsonObj.ployArr[i].status,
      jsonObj.ployArr[i].userid,
      jsonObj.ployArr[i].bindType,
      jsonObj.ployArr[i].bindData,
      jsonObj.ployArr[i].timeZone));
    for (var j in jsonObj.ployOperateArr) {
      if (jsonObj.ployOperateArr[j].ployid == user.ployArray[i].id) {
        localtime = getLocalTime(jsonObj.ployOperateArr[j].hours, jsonObj.ployOperateArr[j].minutes, timeZoneOffset);
        user.ployArray[i].operateArray.push(new PloyOperate(
          jsonObj.ployOperateArr[j].id,
          localtime.localhours,
          localtime.localminutes,
          jsonObj.ployOperateArr[j].operateType,
          jsonObj.ployOperateArr[j].operateParam,
          jsonObj.ployOperateArr[j].ployid));
      }
    }
    for (var j in user.groupSet) {
      if (user.ployArray[i].bindType == 1 && user.ployArray[i].bindParam == user.groupSet[j].addr) {
        user.ployArray[i].groupName = user.groupSet[j].name;
      }
    }
  }
  for (var i = 0; i < user.ployArray.length; i++) {
    user.ployArray[i].operateArray.sort(function(a, b) {
      if (a.hours - b.hours != 0) {
        return a.hours - b.hours;
      } else {
        return a.minutes - b.minutes;
      }
    });
  }

  for (var i = 0; i < user.ployArray.length; i++) {
    if (user.ployArray[i].operateArray.length == 0) {

    } else if (user.ployArray[i].operateArray.length == 1) {
      user.ployArray[i].currentOperate = user.ployArray[i].operateArray[0];
    } else {
      if (user.ployArray[i].operateArray[0].hours >= currentHours && 
          user.ployArray[i].operateArray[0].minutes >= currentMinutes) {
        user.ployArray[i].currentOperate = user.ployArray[i].operateArray[0];
      } else if (user.ployArray[i].operateArray[user.ployArray[i].operateArray.length - 1].hours <= currentHours && 
          user.ployArray[i].operateArray[user.ployArray[i].operateArray.length - 1].minutes <= currentMinutes) {
        user.ployArray[i].currentOperate = user.ployArray[i].operateArray[0];
      } else {
        for (var j = 0; j < user.ployArray[i].operateArray.length - 1; j++) {
          if (parseInt(user.ployArray[i].operateArray[j].hours, 10) * 60 + parseInt(user.ployArray[i].operateArray[j].minutes, 10) < parseInt(currentHours, 10) * 60 + parseInt(currentMinutes, 10) &&
            parseInt(currentHours, 10) * 60 + parseInt(currentMinutes, 10) <= parseInt(user.ployArray[i].operateArray[j+1].hours, 10) * 60 + parseInt(user.ployArray[i].operateArray[j+1].minutes, 10)) {
            user.ployArray[i].currentOperate = user.ployArray[i].operateArray[j+1];
            break;
          }
        }
      }
    }
  }
}

/*********************vue绑定************************/
var jsonObj = ${responseJson};

var user = new User();

userDataRefresh(user, jsonObj);//刷新除了策略之外的数据

ployDataRefresh(user, jsonObj);//刷新策略数据

var operateEditVue = new Vue({
  el: '#operateEditM',
  data: {
    header: "Edit operate",
    operate: {},
    callback: {},
    languageFlag: user.languageFlag,
  },
  methods: {
    show: function(operate, func) {
      this.operate = JSON.parse(JSON.stringify(operate));
      $('#operateEditM').modal('show');
      this.callback = func;
    },
    submitEdit: function() {
      this.operate.hours = operateEditForm.hoursInput.value;
      this.operate.minutes = operateEditForm.minutesInput.value;
      if (this.operate.operateType == 2) {
        this.operate.operateParam = operateEditForm.operateParamInput.value;
      }
      if (this.operate.hours >= 0 && this.operate.hours < 24 && this.operate.minutes >= 0 && this.operate.minutes < 60) {
        $('#operateEditM').modal('hide');
        this.callback(this.operate);
        this.callback = {};
      } else {
        alertVue.show("error","TimeFormatException");
      }
    },
    changeParam: function(param) {
      this.operate.hours = operateEditForm.hoursInput.value;
      this.operate.minutes = operateEditForm.minutesInput.value;
      this.operate.operateParam = param;
    },
    changeType: function(type) {
      this.operate.hours = operateEditForm.hoursInput.value;
      this.operate.minutes = operateEditForm.minutesInput.value;
      this.operate.operateType = type;
      if (type == 1) {
        this.operate.operateParam = 0;
      }
    },
  },
});

var alertVue = new Vue({
  el: '#alertM',
  data: {
    header: {},
    body: {},
    callback: {},
    languageFlag: user.languageFlag,
  },
  methods: {
    show: function(headerStr, bodyStr, callback) {
      this.header = headerStr;
      this.body = bodyStr;
      $('#alertM').modal('show');
      if (callback != null && callback != undefined && callback != "") {
        this.callback = callback;
      }
    },
    buttonAction: function() {
      this.callback();
      this.callback = {};
    }
  },
});

var confirmVue = new Vue({
  el:'#confirmM',
  data: {
    header: {},
    body: {},
    callback: {},
    languageFlag: user.languageFlag,
  },
  methods: {
    show: function(headerStr, bodyStr, func) {
      this.header = headerStr;
      this.body = bodyStr;
      $('#confirmM').modal('show');
      this.callback = func;
    },
    confirmEnter: function(event) {
      $('#confirmM').modal('hide');
      this.callback();
      this.callback = {};
    }
  }
});

var promptF = {};
promptF.header = "";
var promptVue = new Vue({
  el: '#promptM',
  data: {
    header: promptF.header,
    callback: {},
    languageFlag: user.languageFlag,
  },
  methods: {
    show: function(msg, func) {
      this.header = msg;
      $('#promptM').modal('show');
      this.callback = func;
    },
    submit: function() {
      $('#promptM').modal('hide');
      this.callback(promptForm.promptInput.value);
      promptForm.promptInput.value = "";
      this.callback = {};
    }
  },
});

var addPloyVue = new Vue({
  el: '#addPloyM',
  data: {
    header: "Add ploy",
    groups: user.groupSet,
    bindVal: {
      name: "Select group",
    },
    callback: {},
    languageFlag: user.languageFlag,
  },
  methods: {
    show: function(func) {
      $('#addPloyM').modal('show');
      this.callback = func;
    },
    submit: function() {
      if (addPloyForm.ployNameInput.value != "" && addPloyForm.ployNameInput.value != null) {
        if (this.bindVal.addr != null && this.bindVal.addr != undefined && this.bindVal.addr != "") {
          // $('#ployNameSpan').attr('display','none');
          $('#addPloyM').modal('hide');
          this.callback(this.bindVal.addr,addPloyForm.ployNameInput.value);
          addPloyForm.ployNameInput.value = "";
          this.callback = {};
        } else {
          // 请选择绑定分组
        }
      } else {
        // $('#ployNameSpan').attr('display','inline');
      }
    },
    selectBind: function(group) {
      this.bindVal = group;
    },
  },
});

var ployBindVue = new Vue({
  el: '#ployBindM',
  data: {
    header: "Change the binding group",
    groups: user.groupSet,
    bindVal: {
      name: "Select group",
    },
    callback: {},
    languageFlag: user.languageFlag,
  },
  methods: {
    show: function(func) {
      $('#ployBindM').modal('show');
      this.callback = func;
    },
    submit: function() {
      if (this.bindVal.addr != null && this.bindVal.addr != undefined && this.bindVal.addr != "") {
        // $('#ployNameSpan').attr('display','none');
        $('#ployBindM').modal('hide');
        this.callback(this.bindVal.addr);
        this.callback = {};
      } else {
        // 请选择绑定分组
      }
    },
    selectBind: function(group) {
      this.bindVal = group;
    },
  },
});

var devNavVue = new Vue({
  el: '#devNav',
  data: {
    user: user,
    languageFlag: user.languageFlag,
  },
  methods: {},
});

var groupNavVue = new Vue({
  el: '#groupNav',
  data: {
    user: user,
    languageFlag: user.languageFlag,
  },
  methods: {},
});

var ployNavVue = new Vue({
  el: '#ployNav',
  data: {
    user: user,
    languageFlag: user.languageFlag,
  },
  methods: {},
});

var userInfoNavVue = new Vue({
  el: '#userInfoNav',
  data: {
    user: user,
    languageFlag: user.languageFlag,
  },
  methods: {},
});

var testNavVue = new Vue({
  el: '#testNav',
  data: {
    user: user,
    languageFlag: user.languageFlag,
  },
  methods: {},
});

var devDivVue = new Vue({
	el: '#devDiv',
	data: {
		user: user,
		items: user.devSet,
		detail: user.zigbeeSet,
    temperatureF: false,
    languageFlag: user.languageFlag,
	},
	methods: {
    changeLanguage: function (param) {
      if (param == 0) {
        user.languageFlag[0] = 0;
      } else if (param == 1) {
        user.languageFlag[0] = 1;
      }
      operateEditVue.$forceUpdate();
      alertVue.$forceUpdate();
      confirmVue.$forceUpdate();
      promptVue.$forceUpdate();
      addPloyVue.$forceUpdate();
      ployBindVue.$forceUpdate();
      devNavVue.$forceUpdate();
      groupNavVue.$forceUpdate();
      ployNavVue.$forceUpdate();
      userInfoNavVue.$forceUpdate();
      testNavVue.$forceUpdate();
      devDivVue.$forceUpdate();
      groupModelDivVue.$forceUpdate();
      groupDivVue.$forceUpdate();
      ployDivVue.$forceUpdate();
      userDivVue.$forceUpdate();
      testDivVue.$forceUpdate();
    },
    removeOfflineZigbee: function (zigbee) {
      userid = this.user.id;
      $.ajax({
        type:"post",
        url:localhost + "/user/removeZigbee.do",
        data:{
          zigbeeMac:zigbee.mac,
          userid:userid
        },
        async : true,
        datatype: "json",
        success:function(datasource, textStatus, jqXHR) {
          var data = eval('(' + datasource + ')');
          if (data.error == null || data.error == "" || data.error == undefined) {//未出错
            alertVue.show("Submission completed","Command sent successfully!");
            userDataRefresh(user, data);
          } else {
            alertVue.show("error",data.error);
          }
        },
        error: function() {
          alertVue.show("error","Connection failure!");
        }
      });
    },
    turnZigbeeFinder: function(dev, timeCmd) {
      
      $.ajax({
        type:"post",
        url:localhost + "/user/turnZigbeeNetFinder.do",
        data:{
          devMac:dev.mac,
          timeCmd:timeCmd
        },
        async : true,
        datatype: "json",
        success:function(datasource, textStatus, jqXHR) {
          var data = eval('(' + datasource + ')');
          if (data.error == null || data.error == "" || data.error == undefined) {//未出错
            alertVue.show("Submission completed","Command sent successfully!");
            userDataRefresh(user, data);
          } else {
            alertVue.show("error",data.error);
          }
        },
        error: function() {
          alertVue.show("error","Connection failure!");
        }
      });
    },
    temperatureTransform: function(flag) {
      this.temperatureF = flag;
    },
		addDev: function(event) {
			var userid = this.user.id;
      var callbackFunc = function (devMac) {
        if (devMac != null && devMac != "") {
          for (var index in user.devSet) {
            if (user.devSet[index].mac == devMac) {
              alertVue.show("You have added this device","Device named: " + user.devSet[index].name);
              return;
            }
          }
          $.ajax({
            type:"post",
            url:localhost + "/user/addDev.do",
            data:{
              devMac:devMac,
              userid:userid
            },
            async : true,
            datatype: "json",
            success:function(datasource, textStatus, jqXHR) {
              var data = eval('(' + datasource + ')');
              if (data.error == null || data.error == "" || data.error == undefined) {//未出错
                alertVue.show("Submission completed","Added successfully!");
                userDataRefresh(user, data);
              } else {
                alertVue.show("error",data.error);
              }
            },
            error: function() {
              alertVue.show("error","Connection failure!");
            }
          });
        }
      }
      promptVue.show("Please enter the mac address of the controller:", callbackFunc);
			
		},
		removeDev: function(dev) {
      var func = function() {
        $.ajax({
          type:"post",
          url:localhost + "/user/removeDev.do",
          data:{
            devMac:dev.mac,
            userid:user.id
          },
          async : true,
          datatype: "json",
          success:function(datasource, textStatus, jqXHR) {
            var data = eval('(' + datasource + ')');
            if (data.error == null || data.error == "" || data.error == undefined) {//未出错
              alertVue.show("Submission completed","successfully deleted!");
              userDataRefresh(user, data);
            } else {
              alertVue.show("error",data.error);
            }
          },
          error: function() {
            alertVue.show("error","Connection failure!");
          }
        });
      }
      confirmVue.show("Are you sure you want to delete \"" + dev.name + "\"？", "Note: After deleting the controller, all zigbee node data linked under the controller will be deleted.", func);
		},
    renameDev: function (dev) {
      var callbackFunc = function (newName) {
        if (newName != null && newName != "" && newName.length <=16) {
          //ajax请求，返回修改成功或修改失败
          $.ajax({
            type:"post",
            url:localhost + "/user/renameDev.do",
            data:{
              devMac:dev.mac,
              devNewName:newName
            },
            async : true,
            datatype: "json",
            success:function(datasource, textStatus, jqXHR) {
              var data = eval('(' + datasource + ')');
              if (data.error == null || data.error == "" || data.error == undefined) {//未出错
                alertVue.show("Submission completed","Successfully modified!");
                dev.name = newName;
              } else {
                alertVue.show("error",data.error);
              }
            },
            error: function() {
              alertVue.show("error","Connection failure!");
            }
          });
        } else if (newName == "") {//点击取消返回的是null
          alertVue.show("error","New name cannot be empty.");
        } else if (newName.length > 16) {
          alertVue.show("error","The length of the name cannot exceed 16 words.");
        }
      }
      promptVue.show("Please enter a new controller name:", callbackFunc);
    },
    refresh: function () {
      //ajax发送userid重新请求数据
      $.ajax({
        type:"post",
        url:localhost + "/user/refresh.do",
        data:{
          userid:this.user.id,
        },
        async : true,
        datatype: "json",
        success:function(datasource, textStatus, jqXHR) {
          var data = eval('(' + datasource + ')');
          if (data.error == null || data.error == "" || data.error == undefined) {//未出错
            userDataRefresh(user, data);//调用数据更新函数
          } else {
            alertVue.show("error",data.error);
          }
        },
        error: function() {
          alertVue.show("error","Connection failure!");
        }
      });
    },
    showDevDetail: function (dev) {
      alertVue.show("Detail message:",
        "controller name: " + dev.name + "<br/>" +
        "mac address: " + dev.mac + "<br/>" +
        "network: " + dev.online
      );
    },
    switchAll: function(dev, value) {
      $.ajax({
        type:"post",
        url:localhost + "/user/switchByDev.do",
        data:{
          devMac:dev.mac,
          cmd:value
        },
        async : true,
        datatype: "json",
        success:function(datasource, textStatus, jqXHR) {
          var data = eval('(' + datasource + ')');
          if (data.error == null || data.error == "" || data.error == undefined) {//未出错
            alertVue.show("Submission completed","Command sent successfully!");
          } else {
            alertVue.show("error",data.error);
          }
        },
        error: function() {
          alertVue.show("error","Connection failure!");
        }
      });
    },
    setAllBrightness: function (dev) {
      var callbackFunc = function (newBrightness) {
        if (newBrightness != null) {
          if (!Number.isNaN(newBrightness) && newBrightness >= 1 && newBrightness <= 100 && newBrightness.indexOf(".") == -1) {
            $.ajax({
              type:"post",
              url:localhost + "/user/setBrightnessByDev.do",
              data:{
                devMac:dev.mac,
                brightness:newBrightness
              },
              async : true,
              datatype: "json",
              success:function(datasource, textStatus, jqXHR) {
                var data = eval('(' + datasource + ')');
                if (data.error == null || data.error == "" || data.error == undefined) {//未出错
                  alertVue.show("Submission completed","Command sent successfully!");
                  // userDataRefresh(user, data);
                } else {
                  alertVue.show("error",data.error);
                }
              },
              error: function() {
                alertVue.show("error","Connection failure!");
              }
            });
          } else {
            alertVue.show("error","The input is not an integer, or the entered number is not in the range of 1 to 100");
          }
        }
      }
      promptVue.show("Please enter a integer in the range of 1~100", callbackFunc);
    },
    showZigbeeDetail: function (zigbee) {
      var tempString = "node name: " + zigbee.name + "<br/>" 
        + "mac address: " + zigbee.mac + "<br/>" 
        + "controller: " + zigbee.devMac + "<br/>" 
        + "network: " + zigbee.online + "<br/>" 
        + "status: " + zigbee.workStatus + "<br/>" 
        + "brightness: " + zigbee.brightness + "<br/>" 
        + "groups:" + zigbee.groupNames + "<br/>" 
        + "rated power:" + zigbee.power + "W";
      if (zigbee.type == 1) {
        tempString = tempString + "<br/>" + "Device type:" + "钠灯";
        tempString = tempString + "<br/>" + "Dimming range:" + "50 - 100";
      } else if (zigbee.type == 2) {
        tempString = tempString + "<br/>" + "Device type:" + "金卤灯";
        tempString = tempString + "<br/>" + "Dimming range:" + "50 - 100";
      } else if (zigbee.type == 3) {
        tempString = tempString + "<br/>" + "Device type:" + "led灯";
        tempString = tempString + "<br/>" + "Dimming range:" + "1 - 100";
      } else if (zigbee.type == 17) {
        tempString = tempString + "<br/>" + "Device type:" + "钠灯+SHT10温湿度传感器";
        tempString = tempString + "<br/>" + "Dimming range:" + "50 - 100";
      } else if (zigbee.type == 18) {
        tempString = tempString + "<br/>" + "Device type:" + "金卤灯+SHT10温湿度传感器";
        tempString = tempString + "<br/>" + "Dimming range:" + "50 - 100";
      } else if (zigbee.type == 19) {
        tempString = tempString + "<br/>" + "Device type:" + "led灯+SHT10温湿度传感器";
        tempString = tempString + "<br/>" + "Dimming range:" + "1 - 100";
      } else if (zigbee.type == 33) {
        tempString = tempString + "<br/>" + "Device type:" + "SHT10温湿度传感器";
        tempString = tempString + "<br/>" + "Temperature range:" + "-50°C - 125°C";
      } else {
        tempString = tempString + "<br/>" + "Device type:" + "unknown";
        tempString = tempString + "<br/>" + "Dimming range:" + "1 - 100";
      }
      alertVue.show("Detail message", tempString);
    },
    renameZigbee: function (zigbee) {
      var callbackFunc = function (newName) {
        if (newName != null && newName != "" && newName.length <= 16) {
          //ajax请求，返回Successfully modified或修改失败
          $.ajax({
            type:"post",
            url:localhost + "/user/renameZigbee.do",
            data:{
              zigbeeMac:zigbee.mac,
              newName:newName
            },
            async : true,
            datatype: "json",
            success:function(datasource, textStatus, jqXHR) {
              var data = eval('(' + datasource + ')');
              if (data.error == null || data.error == "" || data.error == undefined) {//未出错
                alertVue.show("Submission completed","Successfully modified!");
                zigbee.name = newName;
              } else {
                alertVue.show("error",data.error);
              }
            },
            error: function() {
              alertVue.show("error","Connection failure!");
            }
          });
        } else if (newName == "") {//点击取消返回的是null
          alertVue.show("error","New name cannot be empty.");
        } else if (newName.length > 16) {
          alertVue.show("error","The length of the name cannot exceed 16 words.");
        }
      }
      promptVue.show("Please enter a new node name:", callbackFunc);
    },
    setBrightness: function (zigbee) {
      var callbackFunc = function (newBrightness) {
        if (newBrightness != null) {
          if (!Number.isNaN(newBrightness) && newBrightness >= zigbee.minPower && newBrightness <= 100 && newBrightness.indexOf(".") == -1) {
            $.ajax({
              type:"post",
              url:localhost + "/user/setBrightness.do",
              data:{
                zigbeeMac:zigbee.mac,
                brightness:newBrightness
              },
              async : true,
              datatype: "json",
              success:function(datasource, textStatus, jqXHR) {
                var data = eval('(' + datasource + ')');
                if (data.error == null || data.error == "" || data.error == undefined) {//未出错
                  alertVue.show("Submission completed","Command sent successfully!");
                  // userDataRefresh(user, data);
                } else {
                  alertVue.show("error",data.error);
                }
              },
              error: function() {
                alertVue.show("error","Connection failure!");
              }
            });
          } else {
            alertVue.show("error","The input is not an integer, or the entered number is not in the range of " + zigbee.minPower + "~100");
          }
        }
      }
      promptVue.show("Please enter a integer in the range of " + zigbee.minPower + "~100", callbackFunc);
    },
    switchSingle: function (zigbee, value) {
      $.ajax({
        type:"post",
        url:localhost + "/user/switchZigbee.do",
        data:{
          zigbeeMac:zigbee.mac,
          cmd:value
        },
        async : true,
        datatype: "json",
        success:function(datasource, textStatus, jqXHR) {
          var data = eval('(' + datasource + ')');
          if (data.error == null || data.error == "" || data.error == undefined) {//未出错
            alertVue.show("Submission completed","Command sent successfully!");
          } else {
            alertVue.show("error",data.error);
          }
        },
        error: function() {
          alertVue.show("error","Connection failure!");
        }
      });
    },

	},
  //vue生命周期函数，vue对象创建完成后执行
	created: function() {
	}
});
var groupModelDivVue = new Vue({
  el: '#addToGroupM',
  data: {
    user: user,
    group: {},
    languageFlag: user.languageFlag,
  },
  methods: {
    submitChange: function (event) {//添加zigbee节点到组
      var zigbeeList = new Array();
      var list = $(".zigbeeCheckbox");
      for(var i = 0; i < list.length; i++){
        if (list[i].checked == true) {
          zigbeeList.push(list[i].name.substring(0,list[i].name.length - 3));//将zigbee节点的mac地址加入到zigbeeList中
        }
      }
      if (zigbeeList.length > 0) {//数组不为空
        $.ajax({
          type:"post",
          url:localhost + "/user/addZigbeeToGroup.do",
          data:{
            groupid:this.group.addr,
            zigbeeList:zigbeeList.toString(),
          },
          async : true,
          datatype: "json",
          success:function(datasource, textStatus, jqXHR) {
            var data = eval('(' + datasource + ')');
            if (data.error == null || data.error == "" || data.error == undefined) {//未出错
              alertVue.show("Submission completed","Command sent successfully!");
            } else {
              alertVue.show("error",data.error);
            }
          },
          error: function() {
            alertVue.show("error","Connection failure!");
          }
        });
      } else {
        alertVue.show("error","No node selected!");
      }
    },
  },
});
var groupDivVue = new Vue({
	el: '#groupDiv',
	data: {
    user: user,
		items: user.groupSet,
		detail: user.zigbeeSet,
    temperatureF: false,
    languageFlag: user.languageFlag,
	},
  methods: {
    temperatureTransform: function(flag) {
      this.temperatureF = flag;
    },
    addGroup: function (event) {
      var userid = this.user.id;
      var callbackFunc = function (newGroupName) {
        if (newGroupName != null && newGroupName != "" && newGroupName.length <= 16) {
          for (var index in user.groupSet) {
            if (user.groupSet[index].name == newGroupName) {
              alertVue.show("error","You already have a group with the same name as the input name!");
              return;
            }
          }
          //ajax请求添加控制组
          $.ajax({
            type:"post",
            url:localhost + "/user/addGroup.do",
            data:{
              groupName:newGroupName,
              userid:userid
            },
            async : true,
            datatype: "json",
            success:function(datasource, textStatus, jqXHR) {
              var data = eval('(' + datasource + ')');
              if (data.error == null || data.error == "" || data.error == undefined) {//未出错
                alertVue.show("Submission completed","Added successfully!");
                userDataRefresh(user, data);
              } else {
                alertVue.show("error",data.error);
              }
            },
            error: function() {
              alertVue.show("error","Connection failure!");
            }
          });
        } else if (newGroupName == "") {//点击取消返回的是null
          alertVue.show("error","New name cannot be empty.");
        } else if (newGroupName.length > 16) {
          alertVue.show("error","The length of the name cannot exceed 16 words.");
        }
      }
      promptVue.show("Please enter a new group name:", callbackFunc);
    },
    removeGroup: function (group) {
      var userid = this.user.id;
      $.ajax({
        type:"post",
        url:localhost + "/user/removeGroup.do",
        data:{
          groupid:group.addr,
          userid:userid
        },
        async : true,
        datatype: "json",
        success:function(datasource, textStatus, jqXHR) {
          var data = eval('(' + datasource + ')');
          if (data.error == null || data.error == "" || data.error == undefined) {//未出错
            alertVue.show("Submission completed","successfully deleted!");
            userDataRefresh(user, data);
          } else {
            alertVue.show("error",data.error);
          }
        },
        error: function() {
          alertVue.show("error","Connection failure!");
        }
      });
    },
    renameGroup: function (group) {
      var callbackFunc = function (newName) {
        if (newName != null && newName != "" && newName.length <= 16) {
          for (var index in user.groupSet) {
            if (user.groupSet[index].name == newName) {
              alertVue.show("error","You already have a group with the same name as the input name!");
              return;
            }
          }
          $.ajax({
            type:"post",
            url:localhost + "/user/renameGroup.do",
            data:{
              groupid:group.addr,
              groupNewName:newName
            },
            async : true,
            datatype: "json",
            success:function(datasource, textStatus, jqXHR) {
              var data = eval('(' + datasource + ')');
              if (data.error == null || data.error == "" || data.error == undefined) {//未出错
                alertVue.show("Submission completed","Successfully modified!");
                group.name = newName;
              } else {
                alertVue.show("error",data.error);
              }
            },
            error: function() {
              alertVue.show("error","Connection failure!");
            }
          });
        } else if (newName == "") {//点击取消返回的是null
          alertVue.show("error","New name cannot be empty.");
        } else if (newName.length > 16) {
          alertVue.show("error","The length of the name cannot exceed 16 words.");
        }
      }
      promptVue.show("Please enter a new group name:", callbackFunc);
    },
    refresh: function () {
      //ajax发送userid重新请求数据
      devDivVue.refresh();
    },
    showGroupDetail: function (group) {
      alertVue.show("Detail message:",
        "Group name: " + group.name + "<br/>" +
        "Groupid: " + group.addr
      );
    },
    switchByGroup: function (group,value) {
      $.ajax({
        type:"post",
        url:localhost + "/user/switchByGroup.do",
        data:{
          groupid:group.addr,
          cmd:value
        },
        async : true,
        datatype: "json",
        success:function(datasource, textStatus, jqXHR) {
          var data = eval('(' + datasource + ')');
          if (data.error == null || data.error == "" || data.error == undefined) {//未出错
            alertVue.show("Submission completed","Command sent successfully!");
          } else {
            alertVue.show("error",data.error);
          }
        },
        error: function() {
          alertVue.show("error","Connection failure!");
        }
      });
    },
    addZigbeeToGroup: function(group) {
      groupModelDivVue.group = group;
    },
    removeZigbeeFromGroup: function (zigbee,group) {
      $.ajax({
        type:"post",
        url:localhost + "/user/removeZigbeeFromGroup.do",
        data:{
          zigbeeMac:zigbee.mac,
          groupid:group.addr,
        },
        async : true,
        datatype: "json",
        success:function(datasource, textStatus, jqXHR) {
          var data = eval('(' + datasource + ')');
          if (data.error == null || data.error == "" || data.error == undefined) {//未出错
            alertVue.show("Submission completed","Command sent successfully!");
          } else {
            alertVue.show("error",data.error);
          }
        },
        error: function() {
          alertVue.show("error","Connection failure!");
        }
      });
    },
    setBrightnessByGroup: function (group) {
      var callbackFunc = function (newBrightness) {
        if (newBrightness != null) {
          if (!Number.isNaN(newBrightness) && newBrightness >= 1 && newBrightness <= 100 && newBrightness.indexOf(".") == -1) {
            $.ajax({
              type:"post",
              url:localhost + "/user/setBrightnessByGroup.do",
              data:{
                groupid:group.addr,
                brightness:newBrightness
              },
              async : true,
              datatype: "json",
              success:function(datasource, textStatus, jqXHR) {
                var data = eval('(' + datasource + ')');
                if (data.error == null || data.error == "" || data.error == undefined) {//未出错
                  alertVue.show("Submission completed","Command sent successfully!");
                } else {
                  alertVue.show("error",data.error);
                }
              },
              error: function() {
                alertVue.show("error","Connection failure!");
              }
            });
          } else {
            alertVue.show("error","The input is not an integer, or the entered number is not in the range of 1~100");
          }
        }
      }
      promptVue.show("Please enter a integer in the range of 1~100", callbackFunc);
    },
    showZigbeeDetail: function (zigbee) {
      devDivVue.showZigbeeDetail(zigbee);
    },
    setBrightness: function (zigbee) {
      devDivVue.setBrightness(zigbee);
    },
    switchSingle: function (zigbee, value) {
      devDivVue.switchSingle(zigbee, value);
    },
  }
})
var ployDivVue = new Vue({
	el: '#ployDiv',
	data: {
		items: user.ployArray,
		groups: user.groupSet,
    devs: user.devSet,
    languageFlag: user.languageFlag,
	},
  methods: {
    addPloy: function(event) {//添加策略
      var getMsg = function (groupid, ployName) {//groupid == groupaddr
        if (ployName != null && ployName != "" && ployName.length <= 16) {
          $.ajax({
            type:"post",
            url:localhost + "/user/addPloy.do",
            data:{
              userid: user.id,
              bindType: 1,
              bindData: groupid,
              ployName: ployName,
              timeZone: new Date().getTimezoneOffset(),
            },
            async : true,
            datatype: "json",
            success:function(datasource, textStatus, jqXHR) {
              var data = eval('(' + datasource + ')');
              if (data.error == null || data.error == "" || data.error == undefined) {//未出错
                // alertVue.show("数据",JSON.stringify(data));
                // jsonObj.ployArr = JSON.parse(JSON.stringify(data.ployArr));
                // jsonObj.ployOperateArr = JSON.parse(JSON.stringify(data.ployOperateArr));
                ployDataRefresh(user, data);//刷新策略数据
                alertVue.show("Submission completed","Added successfully");
              } else {
                alertVue.show("error",data.error);
              }
            },
            error: function() {
              alertVue.show("error","Connection failure!");
            }
          });
        } else if (ployName == "") {//点击取消返回的是null
          alertVue.show("error","New name cannot be empty.");
        } else if (ployName.length > 16) {
          alertVue.show("error","The length of the name cannot exceed 16 words");
        }
      }
      addPloyVue.show(getMsg);
    },
    removePloy: function(ploy) {//删除策略
      var func = function() {
        $.ajax({
          type:"post",
          url:localhost + "/user/removePloy.do",
          data:{
            id:ploy.id,
          },
          async : true,
          datatype: "json",
          success:function(datasource, textStatus, jqXHR) {
            var data = eval('(' + datasource + ')');
            if (data.error == null || data.error == "" || data.error == undefined) {//未出错
              alertVue.show("Submission completed","successfully deleted!");
              ployDataRefresh(user, data);//刷新策略数据
            } else {
              alertVue.show("error",data.error);
            }
          },
          error: function() {
            alertVue.show("error","Connection failure!");
          }
        });
      }
      confirmVue.show("Operation confirmation", "Are you sure you want to delete\"" + ploy.name + "\"？", func);
    },
    renamePloy: function(ploy) {//策略重命名
       var callbackFunc = function (newName) {
        if (newName != null && newName != "" && newName.length <= 16) {
          for (var index in user.ployArray) {
            if (user.ployArray[index].name == newName) {
              alertVue.show("error","You already have a ploy with the same name as the input name!");
              return;
            }
          }
          $.ajax({
            type:"post",
            url:localhost + "/user/renamePloy.do",
            data:{
              id:ploy.id,
              newName:newName
            },
            async : true,
            datatype: "json",
            success:function(datasource, textStatus, jqXHR) {
              var data = eval('(' + datasource + ')');
              if (data.error == null || data.error == "" || data.error == undefined) {//未出错
                alertVue.show("Submission completed","Successfully modified!");
                ployDataRefresh(user, data);//刷新策略数据
              } else {
                alertVue.show("error",data.error);
              }
            },
            error: function() {
              alertVue.show("error","Connection failure!");
            }
          });
        } else if (newName == "") {//点击取消返回的是null
          alertVue.show("error","New name cannot be empty.");
        } else if (newName.length > 16) {
          alertVue.show("error","The length of the name cannot exceed 16 words.");
        }
      }
      promptVue.show("Please enter a new ploy name:", callbackFunc);
    },
    refresh: function() {//刷新
      //ajax发送userid重新请求数据
      $.ajax({
        type:"post",
        url:localhost + "/user/ployRefresh.do",
        data:{
          userid:user.id,
        },
        async : true,
        datatype: "json",
        success:function(datasource, textStatus, jqXHR) {
          var data = eval('(' + datasource + ')');
          if (data.error == null || data.error == "" || data.error == undefined) {//未出错
            ployDataRefresh(user, data);//刷新策略数据
            alertVue.show("Submission completed","Refreshed successfully");
          } else {
            alertVue.show("error",data.error);
          }
        },
        error: function() {
          alertVue.show("error","Connection failure!");
        }
      });
    },
    showPloyDetail: function(ploy) {//显示策略详细信息
      var str = "Ploy name: " + ploy.name + "<br/>" + "Ployid: " + ploy.id + "<br/>";
      if (ploy.status == 0) {
        str = str + "Running: false<br/>"
      } else if (ploy.status == 1) {
        str = str + "Running: true<br/>"
      }
      switch (ploy.bindType) {
        case 1:
          str = str + "BindType: group<br/>"
          break;
        case 2:
          str = str + "BindType: controller<br/>"
          break;
        case 3:
          str = str + "BindType: node<br/>"
          break;
        default:
          break;
      }
      str = str + "BindAddress: " + ploy.bindParam + "<br/>";
      alertVue.show("Detail message:", str);
    },
    changeBind: function(ploy) {//改变绑定信息
      var getMsg = function (groupid) {//groupid == groupaddr
        $.ajax({
          type:"post",
          url:localhost + "/user/changePloyBind.do",
          data:{
            id:ploy.id,
            bindType: 1,
            bindData: groupid,
          },
          async : true,
          datatype: "json",
          success:function(datasource, textStatus, jqXHR) {
            var data = eval('(' + datasource + ')');
            if (data.error == null || data.error == "" || data.error == undefined) {//未出错
              ployDataRefresh(user, data);//刷新策略数据
              alertVue.show("Submission completed","Successfully modified。");
            } else {
              alertVue.show("error",data.error);
            }
          },
          error: function() {
            alertVue.show("error","Connection failure!");
          }
        });
      }
      ployBindVue.show(getMsg);
    },
    start: function(ploy) {//开始执行
      if (ploy.editStarted) {
        alertVue.show("Tips","Please start running after stopping editing.");
      } else {
        $.ajax({
          type:"post",
          url:localhost + "/user/ploySwitch.do",
          data:{
            id:ploy.id,
            status:1,
          },
          async : true,
          datatype: "json",
          success:function(datasource, textStatus, jqXHR) {
            var data = eval('(' + datasource + ')');
            if (data.error == null || data.error == "" || data.error == undefined) {//未出错
              ploy.status = 1;
              alertVue.show("Submission completed","Started successfully.");
            } else {
              alertVue.show("error",data.error);
            }
          },
          error: function() {
            alertVue.show("error","Connection failure!");
          }
        });
      }
    },
    stop: function(ploy) {//停止执行
      $.ajax({
          type:"post",
          url:localhost + "/user/ploySwitch.do",
          data:{
            id:ploy.id,
            status:0,
          },
          async : true,
          datatype: "json",
          success:function(datasource, textStatus, jqXHR) {
            var data = eval('(' + datasource + ')');
            if (data.error == null || data.error == "" || data.error == undefined) {//未出错
              ploy.status = 0;
              alertVue.show("Submission completed","Stop running successfully.");
            } else {
              alertVue.show("error",data.error);
            }
          },
          error: function() {
            alertVue.show("error","Connection failure!");
          }
        });
    },
    startEdit: function(ploy) {//开始编辑
      if (ploy.status == 1) {
        alertVue.show("Tips","Please start editing after stopping running.");
      } else {
        ploy.operateBackUp = JSON.stringify(ploy.operateArray);
        ploy.editStarted = true;
      }
    },
    saveEdit: function(ploy) {//保存编辑
      var operateArr = JSON.parse(JSON.stringify(ploy.operateArray));
      var offset = new Date().getTimezoneOffset();
      var gmtTime;
      for (var i in operateArr) {
        gmtTime = getGMTTime(operateArr[i].hours, operateArr[i].minutes, offset);
        operateArr[i].hours = gmtTime.gmthours;
        operateArr[i].minutes = gmtTime.gmtminutes;
      }
      $.ajax({
        type:"post",
        url:localhost + "/user/savePloyEdit.do",
        data:{
          ployid: ploy.id,
          operateArr: JSON.stringify(operateArr),
        },
        async : true,
        datatype: "json",
        success:function(datasource, textStatus, jqXHR) {
          var data = eval('(' + datasource + ')');
          if (data.error == null || data.error == "" || data.error == undefined) {//未出错
            ploy.editStarted = false;
            ploy.operateBackUp = null;
            ployDataRefresh(user, data);

            alertVue.show("Submission completed","Saved successfully.");
          } else {
            alertVue.show("error",data.error);
          }
        },
        error: function() {
          alertVue.show("error","Connection failure!");
        }
      });
    },
    cancelEdit: function(ploy) {//取消编辑
      ploy.operateArray = JSON.parse(ploy.operateBackUp);
      ploy.editStarted = false;
    },
    editOperate: function(ploy, operate) {//指令编辑
      operateEditVue.show(operate, function(param) {
        ploy.operateArray[ploy.operateArray.indexOf(operate)] = param;
        ploy.operateArray.sort(function(a, b) {
          if (a.hours - b.hours != 0) {
            return a.hours - b.hours;
          } else {
            return a.minutes - b.minutes;
          }
        });
      });
    },
    addOperate: function(ploy) {//添加操作
      var temp = new PloyOperate(null,0,0,1,0,ploy.id);
      operateEditVue.show(temp, function(param) {
        ploy.operateArray.push(param);
        ploy.operateArray.sort(function(a, b) {
          if (a.hours - b.hours != 0) {
            return a.hours - b.hours;
          } else {
            return a.minutes - b.minutes;
          }
        });
      });
    },
    removeOperate: function(ploy, operate) {//删除操作
      ploy.operateArray.splice(ploy.operateArray.indexOf(operate), 1);
    },
    removeAllOperate: function(ploy) {
      ploy.operateArray.splice(0, ploy.operateArray.length);
    },
  }
});
var flags = {};
flags.hidden = true;
flags.buttonText = "ChangePassword";
var userDivVue = new Vue({
	el: "#userDiv",
	data: {
		item: user,
    flags: flags,
    languageFlag: user.languageFlag,
	},
  methods: {
    changePassword: function(event) {
      if (flags.hidden == true) {
        flags.hidden = false;
        flags.buttonText = "Cancel";
      } else {
        flags.hidden = true;
        flags.buttonText = "ChangePassword";
      }
    },
    changePasswordEnter: function (event) {
      if (userInfoform.newPassword.value == userInfoform.oldPassword.value) {
        alertVue.show("error","The new password cannot be the same as the old password");
      } else if (userInfoform.newPassword.value != userInfoform.newPasswordRepeat.value) {
        alertVue.show("error","Those passwords didn't match. Try again.");
      } else if (userInfoform.newPassword.value == "" || userInfoform.newPassword.value == null) {
        alertVue.show("error","New password cannot be empty.");
      } else {
        $.ajax({
          type:"post",
          url:localhost + "/user/changePassword.do",
          data:{
            userid:user.id,
            oldPassword:userInfoform.oldPassword.value,
            newPassword:userInfoform.newPassword.value
          },
          async : true,
          datatype: "json",
          success:function(datasource, textStatus, jqXHR) {
            var data = eval('(' + datasource + ')');
            if (data.error == null || data.error == "" || data.error == undefined) {//未出错
              alertVue.show("Submission completed","The password was successfully modified. Please log in again.", function() {
                window.location.href = localhost + "/index.jsp";
              });
              flags.hidden = true;
              flags.buttonText = "ChangePassword";
            } else {
              alertVue.show("error",data.error);
            }
          },
          error: function() {
            alertVue.show("error","Connection failure!");
          }
        });
      }
    }
  }
});
var socketFlag = {};
socketFlag.hidden = true;
var testDivVue = new Vue({
  el: '#testDiv',
  data: {
    user: user,
    socketFlag: socketFlag,
    timeOut: null,
    languageFlag: user.languageFlag,
  },
  methods: {
    sendCmdToAllSocket: function (event) {
      if (socketForm.commandInput.value != null && socketForm.commandInput.value != "") {
        $.ajax({
          type:"post",
          url:localhost + "/user/socketTest.do",
          data:{
            command:socketForm.commandInput.value,
          },
          async : true,
          datatype: "json",
          success:function(datasource, textStatus, jqXHR) {
            var data = eval('(' + datasource + ')');
            if (data.error == null || data.error == "" || data.error == undefined) {//未出错
              socketFlag.hidden = false;
              timeOut = window.setTimeout(msgHidden,1000); 
            } else {
              alertVue.show("error",data.error);
            }
          },
          error: function() {
            alertVue.show("error","Connection failure!");
          }
        });
      }
    },
    turnHeartBeat: function() {
      $.ajax({
        type:"post",
        url:localhost + "/user/turnHeartBeat.do",
        data:{
        },
        async : true,
        datatype: "json",
        success:function(datasource, textStatus, jqXHR) {
          var data = eval('(' + datasource + ')');
          if (data.error == null || data.error == "" || data.error == undefined) {//未出错
            socketFlag.hidden = false;
            timeOut = window.setTimeout(msgHidden,1000); 
          } else {
            alertVue.show("error",data.error);
          }
        },
        error: function() {
          alertVue.show("error","Connection failure!");
        }
      });
    },
  },
});
function msgHidden() {
  socketFlag.hidden = true;
  window.clearTimeout(timeOut);
}
/*********************vue绑定************************/

function passwordTest() {
		if (userInfoform.newPassword.value == userInfoform.newPasswordRepeat.value) {
			passwordTestSpan.setAttribute('style', 'display: none');
		} else {
			passwordTestSpan.setAttribute('style', 'display: inline; color: red');
		}
	}

var pageTimer = {}; //定义定时器全局变量

function intervalRefresh() {
  $.ajax({
    type:"post",
    url:localhost + "/user/refresh.do",
    data:{
      userid:user.id,
    },
    async : true,
    datatype: "json",
    success:function(datasource, textStatus, jqXHR) {
      var data = eval('(' + datasource + ')');
      if (data.error == null || data.error == "" || data.error == undefined) {//未出错
        userDataRefresh(user, data);//调用数据更新函数
      } else {
        alertVue.show("error",data.error);
      }
    },
    error: function() {
      alertVue.show("error","Connection failure!");
      // for(var each in pageTimer){//全部清除方法
      //     window.clearInterval(pageTimer[each]);
      // }
      window.clearInterval(pageTimer.timer1);
    }
  });
}
pageTimer.timer1 = setInterval("intervalRefresh()",5000);//定时刷新，单位毫秒
</script>
</html>

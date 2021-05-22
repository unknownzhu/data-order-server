// /^http(s*):\/\//.test(location.href) || alert('服务错误！');

var objOkTab = "";
layui.use(["element", "layer", "okUtils", "okTab", "okLayer", "jQContextMenu"], function () {
  var okUtils = layui.okUtils;
  var $ = layui.jquery;
  var layer = layui.layer;
  var okLayer = layui.okLayer;

  var okTab = layui.okTab({
    // 菜单请求路径
    url: "data/navs.json",
    // 允许同时选项卡的个数
    openTabNum: 30,
    // 如果返回的结果和navs.json中的数据结构一致可省略这个方法
    parseData: function (data) {
      return data;
    }
  });
  objOkTab = okTab;
  /**
   * 左侧导航渲染完成之后的操作
   */
  okTab.render(function () {
    /**tab栏的鼠标右键事件**/
    $("body .ok-tab").contextMenu({
      width: 'auto',
      itemHeight: 30,
      menu: [
        {
          text: "定位所在页",
          icon: "ok-icon ok-icon-location",
          callback: function () {
            okTab.positionTab();
          }
        },
        {
          text: "关闭当前页",
          icon: "ok-icon ok-icon-roundclose",
          callback: function () {
            okTab.tabClose(1);
          }
        },
        {
          text: "关闭其他页",
          icon: "ok-icon ok-icon-roundclose",
          callback: function () {
            okTab.tabClose(2);
          }
        },
        {
          text: "关闭所有页",
          icon: "ok-icon ok-icon-roundclose",
          callback: function () {
  
            okTab.tabClose(3);
          }
        }
      ]
    });
  });

  /**
   * 添加新窗口
   */
  $("body").on("click", "#navBar .layui-nav-item a", function () {
    // 如果不存在子级
    if ($(this).siblings().length == 0) {
      okTab.tabAdd($(this));
    }
    // 关闭其他展开的二级标签
    // $(this).parent("li").siblings().removeClass("layui-nav-itemed");
    // if (!$(this).attr("lay-id")) {
    //   var topLevelEle = $(this).parents("li.layui-nav-item");
    //   var childs = $("#navBar > li > dl.layui-nav-child").not(topLevelEle.children("dl.layui-nav-child"));
    //   childs.removeAttr("style");
    // }
  });

  /**
   * 移动端的处理事件
   */
  $("body").on("click", ".layui-layout-admin .ok-left a[data-url], .ok-make", function () {
    if ($(".layui-layout-admin").hasClass("ok-left-hide")) {
      $(".layui-layout-admin").removeClass("ok-left-hide");
      $(".ok-menu").find('i').removeClass("ok-menu-hide");
    }
  });

  /**
   * tab左右移动
   */
  $("body").on("click", ".okNavMove", function () {
    var moveId = $(this).attr("data-id");
    var that = this;
    okTab.navMove(moveId, that);
  });
  

  /**
   * 关闭tab页
   */
  $("body").on("click", "#tabAction a", function () {
    var num = $(this).attr("data-num");
    okTab.tabClose(num);
  });


});

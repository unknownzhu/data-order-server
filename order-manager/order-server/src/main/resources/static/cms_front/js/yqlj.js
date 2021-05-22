document.writeln("<style type=\'text/css\'>");
document.writeln("    /*底部开始*/");
document.writeln("    .w1170 *,*:before,.w1170 *:after {box-sizing: border-box;-webkit-box-sizing: border-box;-moz-box-sizing: border-box;}");
document.writeln("    .w1170 body,.w1170 h1,.w1170 h2,.w1170 h3,.w1170 h4,.w1170 h5,.w1170 h6,.w1170 hr,.w1170 p,.w1170 blockquote,.w1170 .w1170 dl,.w1170 dt,.w1170 dd,.w1170 ul,.w1170 ol,.w1170 li,.w1170 pre,.w1170 form,.w1170 fieldset,.w1170 legend,.w1170 button,.w1170 input,.w1170 textarea,.w1170 img{border:medium none;margin:0;padding:0;}");
document.writeln("    .w1170 body{font-size:15px;}");
document.writeln("    .w1170 button,.w1170 input,.w1170 select,.w1170 textarea {font-family:\'Microsoft YaHei\';}");
document.writeln("    .w1170 h1,.w1170 h2,.w1170 h3,.w1170 h4,.w1170 h5,.w1170 h6{ font-size: 100%;}");
document.writeln("    .w1170 em{font-style:normal;}");
document.writeln("    .w1170 ul, ol{list-style: none;}");
document.writeln("    .w1170 table{border-collapse: collapse;border-spacing: 0; }");
document.writeln("    .w1170 a,ins{text-decoration:none;}");
document.writeln("    .w1170 *:focus{outline:none;}");
document.writeln("    .w1170 a{color:#333333;}");
document.writeln("    .w1170 a:hover{color:#355e92;}");
document.writeln("    .w1170 .fl{float:left;}");
document.writeln("    .w1170 .fr{float:right;}");
document.writeln("    .w1170 .cf:after{display:block;content:\'\';height:0;visibility:hidden;clear:both;}");
document.writeln("    .w1170 .cf{zoom:1;}");
document.writeln("    .w1170 body{font-family:\'微软雅黑\';font-size:15px;}");
document.writeln("    .w1170 .hide{display:none;}");
document.writeln("    .w1170 .block{display:block;}");
document.writeln("    .w1170{width:1170px;margin:0 auto;}");
document.writeln("     .friendlink02_div02{width: 1170px;z-index:9999;border-top: 8px solid #00558F;background: #f5f5f5;height:auto;<!--height:396px;-->position: absolute;left:0px;bottom:30px;padding: 0 0 0 20px;overflow:auto!important;;}");
document.writeln("    .friendlink>div a{");
document.writeln("        display: block;");
document.writeln("        margin: 5px 10px;");
document.writeln("        float: left;");
document.writeln("        line-height: 23px;");
document.writeln("        font-size: 13px;");
document.writeln("        width: 139px;");
document.writeln("    }");
document.writeln("    /*友情链接*/");
document.writeln("    .friendlink>.friendlink02_div03 a{width: 90px;}");
document.writeln("    .footer_box_bottom{height: 0px;background: #fff;}");
document.writeln("    .footer_box_bottom>ul{width: 1170px;margin: 0 auto;padding:30px 120px 0 75px;}");
document.writeln("    .footer-nav{display: block;font-size: 13px;line-height: 13px;height: 13px;margin:0 10px 5px 10px;}");
document.writeln("    .footer-separate{width: 1px;height: 13px;background: #4a4a4a;margin: 0 5px;}");
document.writeln("    .footer-l{width:760px;text-align:center;padding-left:30px;}");
document.writeln("    .footer-l p{font-size: 13px;color: #333333;height: 18px;line-height: 18px;margin-top: 6px;}");
document.writeln("    .footer-m{width: 52px;margin-top: 13px;}");
document.writeln("    .footer-m img{display: block;width: 52px;height: 64px;}");
document.writeln("    .footer-r{width: 110px;margin-top: 7px;}");
document.writeln("    .footer-r img{display: block;width: 110px;height: 55px;}");
document.writeln("    .friendlink>div a:hover{color:#004a7c;}");
document.writeln("    .yqljcon{  height: auto !important;  height: 28px;  min-height: 28px;  width: 100%;  padding: 10px 0;  }");
document.writeln("    .footerPP {  text-align: left;  padding-left: 0px;  margin: 0;  height: auto !important;  min-height: 20px;  width: 100%;  }");
document.writeln("    .footxq_left {float: left;  width: 80px;  height: auto !important;  min-height: 10px;  font-size: 13px;  font-weight: 900;  line-height: 26px;  }");
document.writeln("    .footxq_left a {  font-size: 13px;  font-weight: bold;  line-height: 26px;  }");
document.writeln("    .footerPP a {  width: 135px;  color: #333333;  line-height: 23px;  display: block;  float: left;  height: 23px;  overflow: hidden;  }");
document.writeln("    .footxq_right {  float: left;  width: 1022px!important;  height: auto !important;  min-height: 10px; }");
document.writeln("    .footxq_div {width: 100%;height: 26px;}");
document.writeln("    .friendlink02_div02 .footxq_div a {line-height: 22px;width: auto; }");
document.writeln("    /*底部结束*/");
document.writeln("</style>");
document.writeln("<div class=\'screen\'>");
document.writeln("    <div class=\'footer_box_bottom\'>");
document.writeln("    </div>");
document.writeln("</div>");


    
function fontSize(){
    var deviceWidth = $(document).width();
    if(deviceWidth > 768){
        deviceWidth = 768;
    }
    var fontSize = deviceWidth / 7.68;
    $("html").css("fontSize",fontSize);
}
fontSize();
$(window).resize(function(){
    fontSize();
});



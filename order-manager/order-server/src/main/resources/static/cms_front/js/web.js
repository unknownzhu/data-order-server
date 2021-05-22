// 幻灯片FSSlide({id:容器ID，speed:30（速度，越大越快）,pause:200（停留时间，越大越长）,auto:true（是否自动变换）,direction:0（方向 0:向左 1:向上）,circulation:true(是否循环)})
function getCurrStyle(el) {
    if (window.getComputedStyle) {
        if (el.ownerDocument.defaultView.opener) {
            return el.ownerDocument.defaultView.getComputedStyle(el, null);
        }
        return window.getComputedStyle(el, null);
    }
    else {
        return el.currentStyle;
    }
}

function FSSlide(cfg) {
    if (!cfg.id || cfg.id == '') {
        return;
    }
    var _t = this;
    _t.obj = document.getElementById(cfg.id);
    if (_t.obj == null) {
        return;
    }
    var style = getCurrStyle(_t.obj);

    _t.onComplete = cfg.onComplete || null;
    _t.complete = true;
    _t.can = true;
    _t.hasButton = cfg.hasButton == false ? false : true;
    _t.hasList = cfg.hasList == false ? false : true;
    _t.cur = 0;
    _t.speed = cfg.speed || 30;
    _t.pause = cfg.pause || 2000;
    _t.auto = cfg.auto == false ? false : true;
    _t.w = _t.obj.offsetWidth -(parseInt(style.borderLeftWidth) + parseInt(style.borderRightWidth) + parseInt(style.paddingLeft) + parseInt(style.paddingRight));
    _t.h = _t.obj.offsetHeight - (parseInt(style.borderTopWidth) + parseInt(style.borderBottomWidth) + parseInt(style.paddingTop) + parseInt(style.paddingBottom));
    _t.eachNumber = cfg.eachNumber || 1;
    _t.objList = document.getElementById(cfg.id + '_list')
    _t.objIndexList = [];
    _t.circulation = cfg.circulation == true ? true : false;  // 是否循环
    _t.direction = cfg.direction || 0;  // 0向左 1向上
    var objLeft, objRight, objIndex, o, o1, i = 0,
    df = document.createDocumentFragment();
    //_t.num = Math.floor(_t.objList.offsetWidth / _t.w);
    _t.num = _t.objList.getElementsByTagName('li').length/_t.eachNumber;
    if (_t.direction == 0) {
//        _t.offset =  _t.objList.offsetLeft;
        _t.objList.style.width = (_t.w * _t.num) + 'px';
        /*
        _t.objList.style.width = (_t.w * _t.num) + 'px';
        i = 0;
        var os = _t.objList.getElementsByTagName('li');
        while (i < _t.num) {
        os[i].style.width = _t.w + 'px';
        i++;
        }*/
    }
  //  else {
    //    _t.offset =  _t.objList.offsetTop;
   // }
    if (_t.circulation == true) {
        _t.objList.innerHTML += _t.objList.innerHTML;
        if (_t.direction == 0) {
            _t.objList.style.width = (_t.w * _t.num  * 2) + 'px';
        }
    }

    if (_t.hasButton == true) {
        objLeft = document.createElement('span');
        objLeft.className = 'slide_left';
        addEvent(objLeft, 'click', function () { _t.can = true; _t.toPrev(); _t.can = false; });
        df.appendChild(objLeft);

        objRight = document.createElement('span');
        objRight.className = 'slide_right';
        objRight.style.top = objLeft.style.top;
        addEvent(objRight, 'click', function () { _t.can = true; _t.toNext(); _t.can = false; });
        df.appendChild(objRight);
    }
    if (_t.hasList == true) {
        objIndex = document.createElement('ul');
        objIndex.className = 'slide_index';
        i = 0;
        while (i < _t.num) {
            o = document.createElement('li');
            addEvent(o, 'mouseover', function (ti) {
                return function () { _t.to(ti); }
            } (i));
            objIndex.appendChild(o);
            _t.objIndexList.push(o);
            i++;
        }
        df.appendChild(objIndex);
        
        _t.objIndexList[0].className = 'cur';
    }
    _t.obj.appendChild(df);

    if (_t.hasButton == true) {
        /*objLeft.style.top = ((_t.h - objLeft.offsetHeight) / 2) + 'px';
        objRight.style.top = objLeft.style.top;
        objIndex.style.left = ((_t.w - objIndex.offsetWidth) / 2) + 'px';*/
    }
    addEvent(_t.obj, 'mouseover', function () { _t.can = false; });
    addEvent(_t.obj, 'mouseout', function () { _t.can = true; if (_t.auto == true) { _t.toNext1(); } });
    
    if (_t.auto == true) {
        _t.toNext1();
    }
}
FSSlide.prototype = {
    toPrev: function () {
        this.to(this.cur - 1);
    },
    toNext: function () {
        this.to(this.cur + 1);
    },
    toNext1: function () {
        var _t = this;
        if (_t.to1 == true) {
            return;

        }
        _t.to1 = true;
        setTimeout(function () { _t.toNext(); }, _t.pause);
        //this.to(this.cur + 1);
    },
    to: function (i) {
        var _t = this;
        if (i == _t.cur) {
            return;
        }
        if (_t.complete != true || _t.can != true) { if (_t.complete == true) { _t.to1 = false; }; return; }
        _t.complete = false;

        if (i < 0) { i = _t.num - 1; }
        _t.cur1 = i;
        if (_t.circulation == false) {
            if (i >= _t.num) { _t.cur1 = 0; i = 0; }
        }
        else {
            if (i >= _t.num) { _t.cur1 = 0; }
        }
        _t.speed1 = i < _t.cur ? _t.speed : 0 - _t.speed;
        if (_t.direction == 0) { // 向左
//            _t.cl = _t.objList.offsetLeft -_t.offset+_t.w * (_t.cur - i);
		_t.cl = 0-_t.w * i;
        }
        else if (_t.direction == 1) { // 向上
//            _t.cl = _t.objList.offsetTop-_t.offset + _t.h * (_t.cur - i);
		_t.cl = 0-_t.h *  i;
        }

        _t.move();
        if (_t.hasList == true) {
            _t.objIndexList[_t.cur].className = '';
            _t.objIndexList[_t.cur1].className = 'cur';
        }
    },
    move: function () {
        var _t = this, l;
        if (_t.direction == 0) { // 向左
            l = _t.objList.offsetLeft + _t.speed1;
        }
        else if (_t.direction == 1) { // 向上
            l = _t.objList.offsetTop + _t.speed1;
        }
        if (_t.speed1 < 0) {
            if (l < _t.cl) {
                l = _t.cl;
                _t.complete = true;
                _t.to1 = false;
            }
        }
        else {
            if (l > _t.cl) {
                l = _t.cl;
                _t.complete = true;
                _t.to1 = false;
            }
        }
        if (_t.direction == 0) { // 向左
            _t.objList.style.left = l + 'px';
        }
        else if (_t.direction == 1) { // 向上
            _t.objList.style.top = l + 'px';
        }
        if (_t.complete != true) {
            setTimeout(function () { _t.move(); }, 10);
        }
        else {
            _t.cur = _t.cur1;
            if (_t.onComplete != null) {
                _t.onComplete.apply(_t, arguments);
            }

            if (_t.circulation == true && _t.cur == 0) {
                if (_t.direction == 0) { // 向左
                    _t.objList.style.left = '0px';
                }
                else if (_t.direction == 1) { // 向上
                    _t.objList.style.top = '0px';
                }
            }
            if (_t.auto == true && _t.can == true) {
                _t.toNext1();
            }
        }
    }
};

function addEvent(o, sEvent, fun) {
    if (o.addEventListener) {//for DOM;
        o.addEventListener(sEvent, fun, false);
    } else if (o.attachEvent) {
        o.attachEvent("on" + sEvent, fun);
    } else {
        o["on" + sEvent] = fun;
    }
}
function removeEvent(o, sEvent, func) {
    if (o.removeEventListener)
        o.removeEventListener(sEvent, func, false);
    else if (o.detachEvent)
        o.detachEvent("on" + sEvent, func);
    else o["on" + sEvent] = null;
}


addEvent(window, 'load', function () {
    var o = document.getElementById('nav');
    if (o != null) {
        var os = o.childNodes, i = 0, c = os.length, os1, n, m, o1;
        while (i < c) {
            o1 = os[i];
            if (o1.tagName == 'LI' && o1.parentNode == o) {
                o1.onmouseover = function () {
                    this.className += ' hot';

                };
                o1.onmouseout = function () {
                    this.className = this.className.replace(' hot', '');
                };
            }
            i++;
        }
    }
});

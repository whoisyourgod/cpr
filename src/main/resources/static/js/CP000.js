$.ajaxSetup({
    error: function (event) {
        alert(event.responseJSON.message);
        layerClose();
    }
});

function getHrefUrl(pageId) {
    if (pageId == null) {
        pageId = $("#PAGEID").val();
    }
    let url = window.location.href;
    let host = window.location.host;
    let urlsplit = url.split('//')[1].split('/')[1];
    return 'http://' + host + '/' +
        (urlsplit == pageId ? pageId : urlsplit + '/' + pageId);
}

// 空文字チェック
function isNullOrEmpty(val) {
    return val == null || val.length == 0;
}

// 数字チェック
function isNumberChar(val) {
    if (isNullOrEmpty(val)) {
        // 値がない場合はチェックしない
        return true;
    }
    return val.match(/^\d+$/);
}

// 日付フォーマットチェック
function isDate(val, fmt, outDate) {
    // 形式チェック
    if (isNullOrEmpty(val)) {
        // 入力値がない場合はチェックしない
        return true;
    }

    if (!isNumberChar(val.replaceAll('/', '').replaceAll('-', ''))) {
        // 半角数値以外が含まれる場合
        return false;
    }

    // 年月日の先頭インデックス
    let idxY = fmt.indexOf("y");
    let idxM = fmt.indexOf("M");
    let idxD = fmt.indexOf("d");
    // 年の桁数
    let yearLen = (fmt.split("y").length - 1);

    // 年月日デフォルト値(1900年1月1日)
    let year = 0;
    let month = 0;
    let day = 1;

    // 年月日を抽出
    if (idxY != -1) {
        let yearTemp = val.substr(idxY, yearLen);
        if (yearLen == 2) {
            // 下2桁のみの入力の場合
            yearTemp = "20" + yearTemp;
        }
        year = parseInt(yearTemp);
    }
    if (idxM != -1) {
        month = parseInt(val.substr(idxM, 2)) - 1;
    }
    if (idxD != -1) {
        day = parseInt(val.substr(idxD, 2));
    }
    let dt = new Date(year, month, day, 0, 0, 0, 0);

    // フォーマットに含まれない項目はチェックしない
    if (idxY != -1 && dt.getFullYear() != year) {
        return false;
    }
    if (idxM != -1 && dt.getMonth() != month) {
        return false;
    }
    if (idxD != -1 && dt.getDate() != day) {
        return false;
    }

    if (outDate != null) {
        // 取得した値をアウトパラメータに格納
        outDate.setFullYear(year, month, day);
    }
    // チェックOK
    return true;
}

function DateFormat(idate) {
    let sdate = idate.toString();
    let year = parseInt(sdate.substr(0, 4));
    let month = parseInt(sdate.substr(4, 2));
    let day = parseInt(sdate.substr(6, 2));
    if (month < 10) {
        month = '0' + month;
    }

    if (day < 10) {
        day = '0' + day;
    }
    return year + '/' + month + '/' + day;
}

//yyyy/m/d  yyyymmddフォーマット⇒yyyy/mm/dd変更
function commDateFormat(txtDate) {
    let year = txtDate.substr(0, 4);
    let month = txtDate.substr(4, 2);
    let day = txtDate.substr(6, 2);
    if (txtDate.indexOf("/") < 0) {
        year = txtDate.substr(0, 4);
        month = txtDate.substr(4, 2);
        day = txtDate.substr(6, 2);
        return year + "/" + month + "/" + day;
    } else {
        let arr = txtDate.split("/");
        year = arr[0];
        month = (arr[1].length < 2 ? "0" : "") + arr[1];
        day = (arr[2].length < 2 ? "0" : "") + arr[2];
    }

    return year + "/" + month + "/" + day;
}

//日付チェック
function commDateCheck(objId, errMsg) {
    let txtDate = $("#" + objId).val();
    if (txtDate.indexOf("/") < 0 && txtDate.length != 8) {
        alert(errMsg);
        $("#" + objId).focus();
        return false;
    }

    txtDate = commDateFormat(txtDate);
    if (!isDate(txtDate, "yyyy/MM/dd", new Date(0, 0, 0, 0, 0, 0, 0))) {
        alert(errMsg);
        $("#" + objId).focus();
        return false;
    }

    $("#" + objId).val(txtDate);
    return true;
}

//ゼロパディング
function commFormatZero(val, length) {
    val = val + "";
    if (val.length >= length) {
        return val;
    }

    let result = val;
    for (let i = 0; i < length - val.length; i++) {
        result = "0" + result;
    }

    return result;
}

//loadingを開く
function layerOpen(type, fn) {
    if (fn != undefined && fn instanceof Function) {
        fn();
    }

    let box = "";
    switch (type) {
        case 1:
            box = $('#boxWait');
            break;
        case 2:
            box = $('#boxInit');
            break;
        default:
            break;
    }
    $('#layer').css('display', 'block');
    box.css('display', 'flex');
}

// loadingを閉じる
function layerClose(fn) {
    if (fn != undefined && fn instanceof Function) {
        fn();
    }
    $('#layer').removeAttr('style');
    $('.msgBox').removeAttr('style');
    $('#layer').css('display', 'none');
    $('.msgBox').css('display', 'none');
}

//Ajaxで処理，loadingを開く/loadingを閉じる
function commAjax(option) {
    if (option.showLoading == null || option.showLoading == undefined) {
        option.showLoading = true;
    }

    if (option.showLoading) {
        option.complete = function () {
            layerClose();
        };
        layerOpen(1);
    }
    $.ajax(option);
}

//9999⇒9,999
function thousandsQty(num) {
    let str = num.toFixed(0).toString();
    let reg = str.indexOf(".") > -1 ? /(\d)(?=(\d{3})+\.)/g : /(\d)(?=(?:\d{3})+$)/g;
    return str.replace(reg, "$1,");
}

//文字列のlengthチェック
function GetPermitStr(val, length) {
    let str = val.toString();
    let i = 0;
    let cnt = 0;
    for (i = 0; i < str.length; i++) {
        cnt += 1;
        if (str.substr(i, 1).match(/[\uff00-\uffff]/g) || str.substr(i, 1).match(/[\u0800-\u4e00]/g)) {
            cnt += 1;
        }
        if (cnt > length) {
            return false;
        }
    }
    return true;
}

//必ず入力のチェック
function checkRequire(objId, val, errMsg) {
    val = val + "";
    if (val.length == 0) {
        alert(errMsg);
        $("#" + objId).focus();
        return false;
    }
    return true;
}

//文字列のlengthチェック
function checkPermitLength(objId, val, length, errMsg) {
    let str = val.toString();
    let i = 0;
    let cnt = 0;
    for (i = 0; i < str.length; i++) {
        cnt += 1;
        if (str.substr(i, 1).match(/[\uff00-\uffff]/g) || str.substr(i, 1).match(/[\u0800-\u4e00]/g)) {
            cnt += 1;
        }
        if (cnt > length) {
            alert(errMsg);
            $("#" + objId).focus();
            return false;
        }
    }
    return true;
}

//数字のチェック
function checkNum(objId, val, errMsg) {
    if (isNaN(val)) {
        alert(errMsg);
        $("#" + objId).focus();
        return false;
    }
    return true;
}

function commonDateKeyCheck(event) {
    if (event.keyCode != 8 && event.keyCode != 37 && event.keyCode != 39 && event.keyCode != 46 && event.keyCode < 48) {
        event.returnValue = false;
    }

    if (event.keyCode > 57 && event.keyCode < 96) {
        event.returnValue = false;
    }

    if (event.keyCode > 105 && event.keyCode < 109) {
        event.returnValue = false;
    }

    if (event.keyCode > 111 && event.keyCode <= 189) {
        event.returnValue = false;
    }

    if (event.keyCode > 191) {
        event.returnValue = false;
    }
}

$(function () {
    //9,999⇒9999
    $("input[clearThousand]").on("focus", function () {
        $(this).val($(this).val().replaceAll(',', ''));
    });

    //setfocusご全て内容を選択
    $("input").on("focus", function () {
        $(this).select();
    });

    //0
    $("input[formatZero]").on("keydown", function (event) {
        if (event.keyCode == 13) {
            let length = $(this).attr("formatZero");
            $(this).val(commFormatZero($(this).val(), length))
        }
    });

    // データリスト全表示
    $("input[datalist]").on('mousedown mouseup keydown keyup', function (e) {
        // Mouse/Key Down時 プレースホルダーへ退避
        if ((e.type === 'mousedown') || (e.type === 'keydown' && (e.keyCode === 38 || e.keyCode === 40))) {
            // 編集中は何もしない
            if ($(this).val() === '' && $(this).attr('placeholder')) {
                return;
            }
            $(this).attr('placeholder', $(this).val());
            $(this).val('');
            return;
        }

        // Mouse/Key Up時 プレースホルダーから復元
        if ((e.type === 'mouseup') || (e.type === 'keyup' && (e.keyCode === 38 || e.keyCode === 40))) {
            $(this).val($(this).attr('placeholder'));
            $(this).removeAttr('placeholder');
        }
    })

    // ドラッグ＆ドロップ対応
    $("input[datalist]").on('blur', function (e) {
        if ($(this).val() === '' && $(this).attr('placeholder')) {
            $(this).val($(this).attr('placeholder'));
            $(this).removeAttr('placeholder');
        }
    })
});
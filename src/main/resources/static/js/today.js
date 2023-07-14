function getNowDateWeek() {
    let date = new Date();

    let day1 = date.getDay();
    
    let year = date.getFullYear();

    let month = date.getMonth() + 1;

    let weeks = new Array("日","月","火","水","木","金","土");
    
    let week = weeks[day1];

    let day = date.getDate();
    
    if (month < 10) {
        month = '0' + month;
    }

    if (day < 10) {
        day = '0' + day;
    }

    return year + '年' + month + '月' + day + '日' + '(' + week + ')';
}

//今日の日付取得 "年/月/日"
function getNowDate() {
    let date = new Date();
   
    let year = date.getFullYear();

    let month = date.getMonth() + 1;

    let day = date.getDate();
    
    if (month < 10) {
        month = '0' + month;
    }

    if (day < 10) {
        day = '0' + day;
    }

    return year + '/' +  month + '/' + day;
}

//今日の日付取得 "年/月/日"
function getNowTime() {
    let date = new Date();
    
    let year = date.getFullYear();

    let month = date.getMonth() + 1;

    let day = date.getDate();
    
    let hour = date.getHours();
    
    let minute = date.getMinutes();
    
    let second = date.getSeconds();
    
    if (month < 10) {
        month = '0' + month;
    }

    if (day < 10) {
        day = '0' + day;
    }

    if (hour < 10) {
        hour = '0' + hour;
    }

    if (minute < 10) {
        minute = '0' + minute;
    }
    
    if (second < 10) {
        second = '0' + second;
    }   
    return year + '-'  + month + '-'  + day + ' ' + hour + ':' + minute + ':' + second;
}
function qrCode(_this,bookid) {
    _this.innerHTML = "loading…"
    $.ajax({
        url: "/audio/lixue/getQR/?bookId=" + bookid,
        type: "get"
    }).done(function(data) {
        if(data.code == "0000000"){
            var imgUrl =  data.data;
            _this.innerHTML = "查看";
            layer.msg('生成成功！点击查看');
            $(_this).attr("onclick","showPic('" + imgUrl + "')");
        } else {
            layer.msg('生成失败，请重试！');
            _this.innerHTML = "生成";
        }
    });
}

function showPic(url){
    layer.open({
        type: 1,
        title: false,
        closeBtn: 1,
        shadeClose: true,
        skin: 'yourclass',
        content: "<img src=" + url + " style='width:100%' />"
    });
}

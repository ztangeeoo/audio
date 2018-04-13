function qrCode(_this,bookid) {
    $.ajax({
        url: "/audio/lixue/getQR/?bookId=" + bookid,
        type: "get"
    }).done(function(data) {
        if(data.code == "0000000"){
            alert("二维码生成成功！");
            console.log(_this.innerHTML = '222')
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

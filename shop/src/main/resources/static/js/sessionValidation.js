let sessionValidation = {
    init : function (){
        const sessionId = $('#session-mem_id').val();

        if(sessionId == null){
            alert('비 정상적인 접근입니다.');
            window.location.href='/';
        }
    }
}

sessionValidation.init();
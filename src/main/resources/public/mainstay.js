//(function worker() {
//    $.ajax({
//        url: 'json',
//        success: function(data) {
//            var obj = jQuery.parseJSON( data )
//            $('#averageLoad').html(obj.averageLoad)
//            $('#systemCpuLoad').html(obj.systemCpuLoad)
//            $('#form').attr('disabled', obj.disabled === "disabled")
//            if (obj.disabled === "disabled") {
//                $("#timeout").val(obj.timeout)
//                if (obj.oneIsDefault) { $("#oneIsDefault").prop('checked', true)}
//                if (obj.twoIsDefault) { $("#twoIsDefault").prop('checked', true)}
//                if (obj.fourIsDefault) { $("#fourIsDefault").prop('checked', true)}
//                if (obj.eightIsDefault) { $("#eightIsDefault").prop('checked', true)}
//            }
//        },
//        complete: function() {
//            // Schedule the next request when the current one's complete
//            setTimeout(worker, 2000);
//        }
//    });
//})();
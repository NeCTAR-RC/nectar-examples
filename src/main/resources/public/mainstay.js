(function worker() {
    $.ajax({
        url: 'json',
        success: function(data) {
            var obj = jQuery.parseJSON( data )
            $('#averageLoad').html(obj.averageLoad)
            $('#systemCpuLoad').html(obj.systemCpuLoad)
            $('#form').attr('disabled', obj.disabled === "disabled")
        },
        complete: function() {
            // Schedule the next request when the current one's complete
            setTimeout(worker, 2000);
        }
    });
})();
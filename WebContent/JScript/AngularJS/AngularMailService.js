mailApp.service('AngularMailService', function($window) {

	var scope = angular.element($("#body")).scope();

	return ({
		message : message,
		loadInbox : loadInbox,
		loadOutbox : loadOutbox,
		deleteSelected : deleteSelected,
		fav : fav
	});

	function message(data) {
		ShowLoader();
		$.ajax({
			type : "POST",
			url : "/SwiftMail/message.htm",
			data : "data=" + JSON.stringify(data),
			datatype : "json",
			success : function(response) {
				HideLoader();
				var scope = angular.element($("#body")).scope();
				scope.$apply(function() {
					scope.inbox = [];
					scope.inbox = response.outMessages;
					scope.length = scope.inbox.length;
					for (var i = 0; i < scope.inbox.length; i++) {
						if (scope.inbox[i].isActive == 2) {
							scope.inbox[i].checkStar = true;
						}
					}
					if (scope.length != '') {
						scope.view = 'Mail sent successfully';
						$('#popup').stop().fadeIn();
						$('#popup').fadeIn().delay(2000).fadeOut();
					}
				});
			}
		});
	}

	function loadInbox() {
		Loading()
		$.ajax({
			type : "POST",
			url : "/SwiftMail/loadInbox.htm",
			datatype : "json",
			success : function(response) {
				closeLoading();
				var scope = angular.element($("#body")).scope();
				scope.$apply(function() {
					scope.inbox = [];
					scope.inbox = response.inboxMessages;
					scope.inboxlength = scope.inbox.length;
					for (var i = 0; i < scope.inbox.length; i++) {
						if (scope.inbox[i].isActive == 2) {
							scope.inbox[i].checkStar = true;
						}
					}
				});
			}
		});
	}

	function loadOutbox() {
		Loading();
		$.ajax({
			type : "GET",
			url : "/SwiftMail/loadOutbox.htm",
			datatype : "json",
			timeout : 90000,
			success : function(response) {
				closeLoading();
				var scope = angular.element($("#body")).scope();
				scope.$apply(function() {
					scope.inbox = response.outboxMessages;
					for (var i = 0; i < scope.inbox.length; i++) {
						if (scope.inbox[i].isActive == 2) {
							scope.inbox[i].checkStar = true;
						}
					}
				});
			},
			error : function(x, t, m) {
				if (t === "error") {
					$('#exampleModall').modal('show');
				}
			}
		});

	}

	function deleteSelected(data) {
		ShowLoader()
		$.ajax({
			type : "POST",
			url : "/SwiftMail/deleteSelected.htm",
			data : "data=" + JSON.stringify(data),
			datatype : "json",
			success : function(response) {
				HideLoader();
				var scope = angular.element($("#body")).scope();
				scope.$apply(function() {
					scope.delStatus = response.status;
					if (scope.delStatus == true) {
						scope.inbox = [];
						scope.SerialNumbers = [];
						scope.view = 'Mail deleted successfully';
						$('#popup').stop().fadeIn();
						$('#popup').fadeIn().delay(2000).fadeOut();
					}
				});
			}
		});
	}
	function fav(favStatus) {
		ShowLoader()
		$.ajax({
			type : "POST",
			url : "/SwiftMail/favourites.htm",
			data : "data=" + JSON.stringify(favStatus),
			datatype : "json",
			success : function(response) {
				HideLoader();
				var scope = angular.element($("#body")).scope();
				scope.$apply(function() {
					scope.status = response.status;
					if (scope.status) {
						scope.view = 'favorite changed successfully';
						$('#popup').stop().fadeIn();
						$('#popup').fadeIn().delay(2000).fadeOut();
					}
				});
			}

		});
	}

	function ShowLoader() {
		$('#pageLoaderImage').fadeIn('fast', function() {
		});
	}
	function HideLoader() {
		$('#pageLoaderImage').fadeOut('fast', function() {
		});
	}

	function Loading() {
		scope.view = 'Loading ...';
		$('#popup').stop();
		$('#popup').fadeIn();
	}

	function closeLoading() {
		$('#popup').fadeOut();
	}
});
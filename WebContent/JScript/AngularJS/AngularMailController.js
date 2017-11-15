mailApp.controller('mailController', function($scope, AngularMailService) {

	$("#popup").hide();
	$scope.inboxOutbox = true;
	$scope.showMail = false;
	$scope.multipleSelect = true;
	$scope.goback = false;
	$scope.mailContent = {};
	$scope.currentPage = 0;
	$scope.pageSize = 17;
	$scope.type = '1';
	$scope.refreshbtn = true;

	$scope.numberOfPages = function() {
		return Math.ceil($scope.inbox.length / $scope.pageSize);
	}
	$scope.loadInbox = function() {
		$scope.currentPage = 0;
		$('.inbox').addClass('cls');
		$('.outbox').removeClass('cls');
		$scope.SerialNumbers = [];
		$scope.type = '1';
		$scope.all = false;
		$scope.inboxOutbox = true;
		$scope.showMail = false;
		$scope.toFrom = 'to'
		$scope.goback = false;
		$scope.refreshbtn = true;
		$scope.multipleSelect = true;
		$scope.Search = '';
		AngularMailService.loadInbox();
	}
	$scope.loadInbox();

	$scope.sendMail = function(data) {
		$scope.currentPage = 0;
		$('.inbox').removeClass('cls');
		$('.outbox').addClass('cls');
		$scope.type = '2';
		$scope.inboxOutbox = true;
		$scope.showMail = false;
		$scope.toFrom = 'to'
		$scope.goback = false;
		$scope.multipleSelect = true;
		$scope.refreshbtn = true;
		$scope.Search = '';
		AngularMailService.message(data);
	}

	$scope.out = function() {
		$scope.currentPage = 0;
		$('.inbox').removeClass('cls');
		$('.outbox').addClass('cls');
		$scope.SerialNumbers = [];
		$scope.type = '2';
		$scope.all = false;
		$scope.inboxOutbox = true;
		$scope.showMail = false;
		$scope.toFrom = 'from'
		$scope.goback = false;
		$scope.multipleSelect = true;
		$scope.refreshbtn = true;
		$scope.Search = '';
		AngularMailService.loadOutbox();
	}
	$scope.refresh = function() {
		if ($scope.type == 1) {
			AngularMailService.loadInbox();
		} else {
			AngularMailService.loadOutbox();
		}
		$scope.currentPage = 0;
		$scope.all = false;
		$scope.SerialNumbers = [];
	}
	$scope.inboxOutbox = true;
	$scope.showMail = false;

	$scope.Serials = [];
	$scope.checkbox = false;
	$scope.checked = function(checkbox, seriall) {

		var tempObj = {
			serial : '',
		}
		if (!checkbox) {
			tempObj.serial = seriall;
			$scope.SerialNumbers.push(tempObj);
		} else {
			for (var i = 0; i < $scope.SerialNumbers.length; i++) {
				if ($scope.SerialNumbers[i].serial == seriall) {
					$scope.SerialNumbers.splice(i, 1);
					break;
				}
			}

		}
	}

	$scope.selectedAll = false;
	$scope.checkAll = function(all) {
		var tempObj = {
			serial : '',
		}
		for (var i = 0; i < $scope.inbox.length; i++) {
			$scope.inbox[i].checkbox = !all;
			tempObj.serial = $scope.inbox[i].serial;
			$scope.SerialNumbers.push(tempObj);
		}
		if (all) {
			$scope.SerialNumbers = [];
		}

	}

	$scope.deleteSelected = function(a) {
		$scope.Serials = [];
		var tempdata = {
			listmail : [],
		};
		if ($scope.all) {
			for (var k = 0; k < $scope.filteredmails.length; k++) {
				var filteredkeys = $scope.filteredmails[k].serial;
				if ($scope.filteredmails[k].checkbox) {
					$scope.Serials.push(filteredkeys);
				}
			}
			for (var i = 0; i < $scope.Serials.length; i++) {
				var tempObj = {
					serial : '',
				}
				tempObj.serial = $scope.Serials[i];
				tempdata.listmail.push(tempObj);
			}
			$scope.all = false;
			$scope.Search = '';
			$scope.inbox = [];
			AngularMailService.deleteSelected(tempdata);

			setTimeout(function() {
				$scope.goLoad()
			}, 1000);

		} else if ($scope.SerialNumbers != '') {
			tempdata.listmail = $scope.SerialNumbers

			$scope.showMail = false;
			$scope.inboxOutbox = true;
			$scope.goback = false;
			$scope.multipleSelect = true;
			$scope.refreshbtn = true;
			$scope.all = false;
			$scope.Search = '';
			$scope.inbox = [];
			AngularMailService.deleteSelected(tempdata);

		} else if (a == 'deleteall') {
			for (var k = 0; k < $scope.filteredmails.length; k++) {
				var filteredkeys = $scope.filteredmails[k].serial;
				$scope.Serials.push(filteredkeys);
			}
			for (var i = 0; i < $scope.Serials.length; i++) {
				var tempObj = {
					serial : '',
				}
				tempObj.serial = $scope.Serials[i];
				tempdata.listmail.push(tempObj);
			}
			$scope.all = false;
			$scope.Search = '';
			$scope.inbox = [];
			AngularMailService.deleteSelected(tempdata);

			setTimeout(function() {
				$scope.goLoad()
			}, 1000);

		} else {
			alert('Select mails to delete');
		}
		$scope.currentPage = 0;
		setTimeout(function() {
			$scope.goLoad()
		}, 1000);
	}

	$scope.none = function() {
		$scope.all = false;
		for (var i = 0; i < $scope.inbox.length; i++) {
			$scope.inbox[i].checkbox = false;
		}
	}
	$scope.clear = function() {
		$scope.mail.subject = '';
		$scope.mail.address = '';
		$scope.mail.message = '';
	}

	$scope.fav = function(serial, Status) {
		if (Status) {
			var isActive = 1;
		} else {
			var isActive = 2;
		}
		$scope.favStatus = {
			serial : serial,
			isActive : isActive
		}
		AngularMailService.fav($scope.favStatus);
		for (var i = 0; i < $scope.inbox.length; i++) {
			if ($scope.inbox[i].serial == serial) {
				$scope.inbox[i].isActive = Status;
			}
		}
	}

	$scope.showmail = function(serial) {
		$scope.inboxOutbox = false;
		$scope.showMail = true;
		$scope.multipleSelect = false;
		$scope.goback = true;
		$scope.refreshbtn = false;
		for (var i = 0; i < $scope.inbox.length; i++) {
			if ($scope.inbox[i].serial == serial) {
				$scope.mailContent = {
					mail : $scope.inbox[i].address,
					message : $scope.inbox[i].message,
					subject : $scope.inbox[i].subject,
					serial : $scope.inbox[i].serial,
					time : $scope.inbox[i].time
				}
				if ($scope.inbox[i].isActive == 1) {
					$scope.mailContent.isActive = false;
					break;
				} else {
					$scope.mailContent.isActive = true;
					break;
				}
			}
		}
		var tempObj = {
			serial : '',
		}
		tempObj.serial = $scope.mailContent.serial;
		$scope.SerialNumbers.push(tempObj);
	}
	$scope.openReply = function(to) {
		$scope.mail.address = to;
	}

	$scope.back = function() {
		if ($scope.type == 1) {
			AngularMailService.loadInbox();
		} else {
			AngularMailService.loadOutbox();
		}
		$scope.multipleSelect = true;
		$scope.goback = false;
		$scope.refreshbtn = true;
		$scope.showMail = false;
		$scope.inboxOutbox = true;
		$scope.SerialNumbers = [];
	}

	$scope.set_color = function(mail) {

		if (mail == true) {
			return {
				background : "#FFC"
			}
		}
	}

	$scope.goLoad = function() {
		if ($scope.type == 1) {
			AngularMailService.loadInbox();
		} else if ($scope.type == 2) {
			AngularMailService.loadOutbox();
		}
	}

	$scope.reloadPage = function() {
		window.location.reload();
	}

});
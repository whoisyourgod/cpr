$(function() {
	// 現在の日付取得
    $("#NOWDATE").text(getNowDateWeek());
    
	// 現在の日付取得
	var dt = new Date();
	var y = dt.getFullYear();
	var m = ("00" + (dt.getMonth() + 1)).slice(-2);
	var d = ("00" + dt.getDate()).slice(-2);
	$("#s_ymd").val(y + m + d);

	// 表示ボタン押下
	$("#SEARCH").on("click", function() {
		search_info();
	});

	// 自動更新停止ボタン押下
	$("#STOP").on("click", function() {
		stp_onclick();
	});
});

function search_info() {
	auto_rep();
	var id = setInterval(function() {
		if (document.frm.kbn.value != "0") {
			clearInterval(id);
		} else {
			auto_rep();
		}
	}, 5000);
}

function drowBarcode(strID, strBar) {
	JsBarcode(strID, strBar, { width: 1, height: 15, displayValue: false });
}

function auto_rep() {
	if (document.getElementById("tbl") != null) {
		var div = document.getElementById("div");
		bdy.removeChild(div);
	}

	if ($("#soko").val() == 1) {
		var st = "1" + $("#STNO").val();
	} else {
		var st = "2" + $("#STNO").val() - 110;
	}

	$.ajax({
		type: "get",
		contentType: "application/json",
		url: "CP100/cpSearch",
		async: false,
		data: {
			STNO: st
		},
		success: function(result) {
			var tane_cnt = 0;
			var str = [];

			if (result != null && result.length > 0) {
				$("#hiki").val(result[0].toOki);
				$("#okiba").val(result[0].okiNm);
				$("#syain").val(result[0].empCd);
				$("#syanm").val(result[0].empNm);
				if (result[0].tokuCd !== null) {
					$("#tok").val(result[0].tokuCd);
					$("#toknm").val(result[0].tokuNm);
				}
				$("#comm").val(result[0].comm);
				var tban = result[0].fromTana;
				$("#tana").val(tban.substr(0, 1) + "-" + tban.substr(1, 2) + "-" + tban.substr(3, 3) + "-" + tban.substr(6, 2));
				$("#sno").val(result[0].snNo);
				$("#mai").val(result[0].maisu);
				var div = document.createElement('div');
				div.id = "div";
				div.className = "container-fluid";
				var table = document.createElement('table');
				table.id = "tbl";
				table.cellpadding = "0";
				var tr = document.createElement('tr');
				var th = document.createElement('th');
				th.innerHTML = "ロケ";
				th.id = "th0"
				th.width = '80';
				tr.appendChild(th);
				var th = document.createElement('th');
				th.innerHTML = "商品ＣＤ";
				th.id = "th0"
				th.width = '80';
				tr.appendChild(th);
				var th = document.createElement('th');
				th.innerHTML = "ＪＡＮ";
				th.id = "th0"
				th.width = '120';
				tr.appendChild(th);
				var th = document.createElement('th');
				th.innerHTML = "商　品　名";
				th.id = "th0"
				th.width = '220';
				th.colSpan = "2";
				tr.appendChild(th);
				var th = document.createElement('th');
				th.innerHTML = "ロット";
				th.id = "th0"
				tr.appendChild(th);
				var th = document.createElement('th');
				th.innerHTML = "入数";
				th.id = "th0"
				tr.appendChild(th);
				var th = document.createElement('th');
				th.innerHTML = "ケース数";
				th.id = "th0"
				tr.appendChild(th);
				var th = document.createElement('th');
				th.innerHTML = "数量";
				th.id = "th0"
				tr.appendChild(th);
				var th = document.createElement('th');
				th.innerHTML = "状態ＣＤ";
				th.id = "th0"
				tr.appendChild(th);
				var th = document.createElement('th');
				th.innerHTML = "状態";
				th.id = "th0"
				tr.appendChild(th);
				var th = document.createElement('th');
				th.innerHTML = "品質異常№";
				th.id = "th0"
				tr.appendChild(th);
				table.appendChild(tr);
				for (let i = 0; i < result.length; i++) {
					var tr = document.createElement('tr');
					var td = document.createElement('td');
					td.textContent = result[i].location;
					td.id = "td0"
					tr.appendChild(td);

					var td = document.createElement('td');
					td.textContent = result[i].shohinCd;
					td.id = "td0"
					tr.appendChild(td);

					var td = document.createElement('td');
					td.textContent = result[i].janCd;
					td.id = "td0"
					tr.appendChild(td);

					var td = document.createElement('td');
					td.textContent = result[i].shohinNm;
					td.id = "td0"
					td.colSpan = "2";
					tr.appendChild(td);

					var td = document.createElement('td');
					td.textContent = result[i].lotYear;
					td.id = "td0"
					tr.appendChild(td);

					var td = document.createElement('td');
					td.textContent = result[i].ksHaisu;
					td.id = "td0"
					tr.appendChild(td);

					var td = document.createElement('td');
					td.textContent = result[i].ksSu;
					td.id = "td0"
					tr.appendChild(td);

					var td = document.createElement('td');
					td.textContent = result[i].suryo;
					td.id = "td0"
					tr.appendChild(td);

					var td = document.createElement('td');
					td.textContent = result[i].statusCd;
					td.id = "td0"
					tr.appendChild(td);

					var td = document.createElement('td');
					td.textContent = result[i].jyt;
					td.id = "td0"
					tr.appendChild(td);

					var td = document.createElement('td');
					td.textContent = result[i].ijoNo;
					td.id = "td0"
					tr.appendChild(td);

					table.appendChild(tr);

					var meisai = 0;
					// IRIS 検品データ（ケース）読込
					if ($("#cs_as").val() == 0 || $("#cs_as").val() == 1 || $("#kubun").val() == 0 || $("#kubun").val() == 1) {
						$.ajax({
							type: "get",
							contentType: "application/json",
							url: "CP100/csSearch",
							async: false,
							data: {
								YMD: $("#s_ymd").val(),
								SYOCD: result[i].shohinCd
							},
							success: function(result_cs) {
								if (result_cs.length > 0) {
									meisai = 1;
									var tr = document.createElement('tr');
									var th = document.createElement('th');
									th.innerHTML = "合計出荷数";
									th.id = 'th1';
									th.style.backgroundColor = '#CCFFCC';
									tr.appendChild(th);
									var th = document.createElement('th');
									th.innerHTML = "運送会社";
									th.id = 'th1';
									th.style.backgroundColor = '#CCFFCC';
									th.width = '120';
									tr.appendChild(th);
									var th = document.createElement('th');
									th.innerHTML = "出荷数";
									th.id = 'th1';
									th.style.backgroundColor = '#CCFFCC';
									th.width = '80';
									tr.appendChild(th);
									var th = document.createElement('th');
									th.innerHTML = "種まき№";
									th.id = 'th1';
									th.style.backgroundColor = '#CCFFCC';
									th.width = '120';
									tr.appendChild(th);
									var th = document.createElement('th');
									th.innerHTML = "出荷数";
									th.id = 'th1';
									th.style.backgroundColor = '#CCFFCC';
									th.width = '80';
									tr.appendChild(th);
									var th = document.createElement('th');
									th.innerHTML = "種まき№";
									th.id = 'th1';
									th.style.backgroundColor = '#CCFFCC';
									th.width = '120';
									th.colspan = '2';
									tr.appendChild(th);
									var th = document.createElement('th');
									th.innerHTML = "出荷数";
									th.id = 'th1';
									th.style.backgroundColor = '#CCFFCC';
									th.width = '80';
									tr.appendChild(th);
									var th = document.createElement('th');
									th.innerHTML = "種まき№";
									th.id = 'th1';
									th.style.backgroundColor = '#CCFFCC';
									th.width = '120';
									tr.appendChild(th);
									var th = document.createElement('th');
									th.innerHTML = "出荷数";
									th.id = 'th1';
									th.style.backgroundColor = '#CCFFCC';
									th.width = '80';
									tr.appendChild(th);
									var th = document.createElement('th');
									th.innerHTML = "種まき№";
									th.id = 'th1';
									th.style.backgroundColor = '#CCFFCC';
									th.width = '120';
									tr.appendChild(th);
									var th = document.createElement('th');
									th.innerHTML = "出荷数";
									th.id = 'th1';
									th.style.backgroundColor = '#CCFFCC';
									th.width = '80';
									tr.appendChild(th);
									table.appendChild(tr);

									for (let i_cs = 0; i_cs < result_cs.length; i_cs++) {
										var tr = document.createElement('tr');
										var td = document.createElement('td');
										td.textContent = result_cs[i_cs].cs;
										td.id = 'td1';
										td.rowSpan = "2";
										td.style.backgroundColor = '#CCFFCC';
										tr.appendChild(td);
										var td = document.createElement('td');
										td.textContent = result_cs[i_cs].unsmei;
										td.id = 'td1';
										td.rowSpan = "2";
										td.style.backgroundColor = '#CCFFCC';
										tr.appendChild(td);
										var td = document.createElement('td');
										td.textContent = result_cs[i_cs].csUnsmei;
										td.id = 'td1';
										td.rowSpan = "2";
										td.style.backgroundColor = '#CCFFCC';
										tr.appendChild(td);
										var tr_img = document.createElement('tr');
										if ($("#tane").val() == 0) {
											if (result_cs[i_cs].taneNo1) {
												var td = document.createElement('td');
												td.textContent = result_cs[i_cs].taneNo1;
												td.id = 'td1';
												td.style.backgroundColor = '#CCFFCC';
												tr.appendChild(td);

												var td = document.createElement('td');
												td.textContent = result_cs[i_cs].csTane1;
												td.id = 'td1';
												td.rowSpan = "2";
												td.style.backgroundColor = '#CCFFCC';
												tr.appendChild(td);

												str[tane_cnt] = result_cs[i_cs].taneNo1;
												var td = document.createElement('td');
												var img = document.createElement('img');
												img.id = 'barcode' + tane_cnt;
												td.appendChild(img);
												td.id = 'td1';
												td.style.backgroundColor = '#CCFFCC';
												tr_img.appendChild(td);

												tane_cnt += 1;
											}

											if (result_cs[i_cs].taneNo2) {
												var td = document.createElement('td');
												td.textContent = result_cs[i_cs].taneNo2;
												td.id = 'td1';
												td.style.backgroundColor = '#CCFFCC';
												tr.appendChild(td);

												var td = document.createElement('td');
												td.textContent = result_cs[i_cs].csTane2;
												td.id = 'td1';
												td.rowSpan = "2";
												td.style.backgroundColor = '#CCFFCC';
												tr.appendChild(td);

												str[tane_cnt] = result_cs[i_cs].taneNo2;
												var td = document.createElement('td');
												var img = document.createElement('img');
												img.id = 'barcode' + tane_cnt;
												td.appendChild(img);
												td.id = 'td1';
												td.style.backgroundColor = '#CCFFCC';
												tr_img.appendChild(td);

												tane_cnt += 1;
											}

											if (result_cs[i_cs].taneNo3) {
												var td = document.createElement('td');
												td.textContent = result_cs[i_cs].taneNo3;
												td.id = 'td1';
												td.style.backgroundColor = '#CCFFCC';
												tr.appendChild(td);

												var td = document.createElement('td');
												td.textContent = result_cs[i_cs].csTane3;
												td.id = 'td1';
												td.rowSpan = "2";
												td.style.backgroundColor = '#CCFFCC';
												tr.appendChild(td);

												str[tane_cnt] = result_cs[i_cs].taneNo3;
												var td = document.createElement('td');
												var img = document.createElement('img');
												img.id = 'barcode' + tane_cnt;
												td.appendChild(img);
												td.id = 'td1';
												td.style.backgroundColor = '#CCFFCC';
												tr_img.appendChild(td);

												tane_cnt += 1;
											}

											if (result_cs[i_cs].taneNo4) {
												var td = document.createElement('td');
												td.textContent = result_cs[i_cs].taneNo4;
												td.id = 'td1';
												td.style.backgroundColor = '#CCFFCC';
												tr.appendChild(td);

												var td = document.createElement('td');
												td.textContent = result_cs[i_cs].csTane4;
												td.id = 'td1';
												td.rowSpan = "2";
												td.style.backgroundColor = '#CCFFCC';
												tr.appendChild(td);

												str[tane_cnt] = result_cs[i_cs].taneNo4;
												var td = document.createElement('td');
												var img = document.createElement('img');
												img.id = 'barcode' + tane_cnt;
												td.appendChild(img);
												td.id = 'td1';
												td.style.backgroundColor = '#CCFFCC';
												tr_img.appendChild(td);

												tane_cnt += 1;
											}
										}
										table.appendChild(tr);
										table.appendChild(tr_img);
									}
								}
							}
						});
					}
					// IRIS 検品データ（アソート）読込
					if ($("#cs_as").val() == 0 || $("#cs_as").val() == 2 || $("#kubun").val() == 0 || $("#kubun").val() == 1) {
						$.ajax({
							type: "get",
							contentType: "application/json",
							url: "CP100/asSearch",
							async: false,
							data: {
								YMD: $("#s_ymd").val(),
								SYOCD: result[i].shohinCd
							},
							success: function(result_as) {
								if (result_as.length > 0) {
									if (meisai != 1) {
										meisai = 1;
										var tr = document.createElement('tr');
										var th = document.createElement('th');
										th.innerHTML = "合計出荷数";
										th.id = 'th1';
										th.style.backgroundColor = '#FFFFCC';
										tr.appendChild(th);
										var th = document.createElement('th');
										th.innerHTML = "運送会社";
										th.id = 'th1';
										th.style.backgroundColor = '#FFFFCC';
										th.width = '120';
										tr.appendChild(th);
										var th = document.createElement('th');
										th.innerHTML = "出荷数";
										th.id = 'th1';
										th.style.backgroundColor = '#FFFFCC';
										th.width = '80';
										tr.appendChild(th);
										var th = document.createElement('th');
										th.innerHTML = "種まき№";
										th.id = 'th1';
										th.style.backgroundColor = '#FFFFCC';
										th.width = '120';
										tr.appendChild(th);
										var th = document.createElement('th');
										th.innerHTML = "出荷数";
										th.id = 'th1';
										th.style.backgroundColor = '#FFFFCC';
										th.width = '80';
										tr.appendChild(th);
										var th = document.createElement('th');
										th.innerHTML = "種まき№";
										th.id = 'th1';
										th.style.backgroundColor = '#FFFFCC';
										th.width = '120';
										th.colspan = '2';
										tr.appendChild(th);
										var th = document.createElement('th');
										th.innerHTML = "出荷数";
										th.id = 'th1';
										th.style.backgroundColor = '#FFFFCC';
										th.width = '80';
										tr.appendChild(th);
										var th = document.createElement('th');
										th.innerHTML = "種まき№";
										th.id = 'th1';
										th.style.backgroundColor = '#FFFFCC';
										th.width = '120';
										tr.appendChild(th);
										var th = document.createElement('th');
										th.innerHTML = "出荷数";
										th.id = 'th1';
										th.style.backgroundColor = '#FFFFCC';
										th.width = '80';
										tr.appendChild(th);
										var th = document.createElement('th');
										th.innerHTML = "種まき№";
										th.id = 'th1';
										th.style.backgroundColor = '#FFFFCC';
										th.width = '120';
										tr.appendChild(th);
										var th = document.createElement('th');
										th.innerHTML = "出荷数";
										th.id = 'th1';
										th.style.backgroundColor = '#FFFFCC';
										th.width = '80';
										tr.appendChild(th);
										table.appendChild(tr);
									}

									for (let i_cs = 0; i_cs < result_as.length; i_cs++) {
										var tr = document.createElement('tr');
										var td = document.createElement('td');
										td.textContent = result_as[i_cs].cs;
										td.id = 'td1';
										td.rowSpan = "2";
										td.style.backgroundColor = '#FFFFCC';
										tr.appendChild(td);
										var td = document.createElement('td');
										td.textContent = result_as[i_cs].unsmei;
										td.id = 'td1';
										td.rowSpan = "2";
										td.style.backgroundColor = '#FFFFCC';
										tr.appendChild(td);
										var td = document.createElement('td');
										td.textContent = result_as[i_cs].csUnsmei;
										td.id = 'td1';
										td.rowSpan = "2";
										td.style.backgroundColor = '#FFFFCC';
										tr.appendChild(td);
										var tr_img = document.createElement('tr');
										if ($("#tane").val() == 0) {
											if (result_as[i_cs].taneNo1) {
												var td = document.createElement('td');
												td.textContent = result_as[i_cs].taneNo1;
												td.id = 'td1';
												td.style.backgroundColor = '#FFFFCC';
												tr.appendChild(td);

												var td = document.createElement('td');
												td.textContent = result_as[i_cs].csTane1;
												td.id = 'td1';
												td.rowSpan = "2";
												td.style.backgroundColor = '#FFFFCC';
												tr.appendChild(td);

												str[tane_cnt] = result_as[i_cs].taneNo1;
												var td = document.createElement('td');
												var img = document.createElement('img');
												img.id = 'barcode' + tane_cnt;
												td.appendChild(img);
												td.id = 'td1';
												td.style.backgroundColor = '#FFFFCC';
												tr_img.appendChild(td);

												tane_cnt += 1;
											}

											if (result_as[i_cs].taneNo2) {
												var td = document.createElement('td');
												td.textContent = result_as[i_cs].taneNo2;
												td.id = 'td1';
												td.style.backgroundColor = '#FFFFCC';
												tr.appendChild(td);

												var td = document.createElement('td');
												td.textContent = result_as[i_cs].csTane2;
												td.id = 'td1';
												td.rowSpan = "2";
												td.style.backgroundColor = '#FFFFCC';
												tr.appendChild(td);

												str[tane_cnt] = result_as[i_cs].taneNo2;
												var td = document.createElement('td');
												var img = document.createElement('img');
												img.id = 'barcode' + tane_cnt;
												td.appendChild(img);
												td.id = 'td1';
												td.style.backgroundColor = '#FFFFCC';
												tr_img.appendChild(td);

												tane_cnt += 1;
											}

											if (result_as[i_cs].taneNo3) {
												var td = document.createElement('td');
												td.textContent = result_as[i_cs].taneNo3;
												td.id = 'td1';
												td.style.backgroundColor = '#FFFFCC';
												tr.appendChild(td);

												var td = document.createElement('td');
												td.textContent = result_as[i_cs].csTane3;
												td.id = 'td1';
												td.rowSpan = "2";
												td.style.backgroundColor = '#FFFFCC';
												tr.appendChild(td);

												str[tane_cnt] = result_as[i_cs].taneNo3;
												var td = document.createElement('td');
												var img = document.createElement('img');
												img.id = 'barcode' + tane_cnt;
												td.appendChild(img);
												td.id = 'td1';
												td.style.backgroundColor = '#FFFFCC';
												tr_img.appendChild(td);

												tane_cnt += 1;
											}

											if (result_as[i_cs].taneNo4) {
												var td = document.createElement('td');
												td.textContent = result_as[i_cs].taneNo4;
												td.id = 'td1';
												td.style.backgroundColor = '#FFFFCC';
												tr.appendChild(td);

												var td = document.createElement('td');
												td.textContent = result_as[i_cs].csTane4;
												td.id = 'td1';
												td.rowSpan = "2";
												td.style.backgroundColor = '#FFFFCC';
												tr.appendChild(td);

												str[tane_cnt] = result_as[i_cs].taneNo4;
												var td = document.createElement('td');
												var img = document.createElement('img');
												img.id = 'barcode' + tane_cnt;
												td.appendChild(img);
												td.id = 'td1';
												td.style.backgroundColor = '#FFFFCC';
												tr_img.appendChild(td);

												tane_cnt += 1;
											}
										}
										table.appendChild(tr);
										table.appendChild(tr_img);
									}
								}
							}
						});
					}
					// AMAZON 検品データ（アマゾン）、PLAZA 検品データ（佐川）、PLAZA 検品データ（Ｌ1Ｍ）
					$.ajax({
						type: "get",
						contentType: "application/json",
						url: "CP100/azPlazaSearch",
						async: false,
						data: {
							YMD: $("#s_ymd").val(),
							SYOCD: result[i].shohinCd
						},
						success: function(result_az_plaza) {
							result_az = result_az_plaza["AMAZON"];
							result_plaza1 = result_az_plaza["PLAZA1"];
							result_plaza2 = result_az_plaza["PLAZA2"];
							// AMAZON 検品データ（アマゾン）
							if ($("#cs_as").val() == 0 || $("#cs_as").val() == 1 || $("#kubun").val() == 0 || $("#kubun").val() == 2) {
								if (result_az!= null && result_az.length > 0) {
									var az = [];
									var ix = 0;
									for (let i_az = 0; i_az < result_az.length; i_az++) {
										switch (result_az[i_az].TODOKECD) {
											case 712465:
												az[ix] = "ＶＦ埼玉";
												break;
											case 500131:
												az[ix] = "ＶＦ米原";
												break;
											case 500230:
												az[ix] = "ＶＦ鳥栖";
												break;
											default:
												var cn = result_az[i_az].FLD_4;
												if (cn.indexOf("①") != -1) {
													az[ix] = "①";
												} else {
													if (cn.indexOf("②") != -1) {
														az[ix] = "②";
													} else {
														if (cn.indexOf("③") != -1) {
															az[ix] = "③";
														} else {
															if (cn.indexOf("④") != -1) {
																az[ix] = "④";
															} else {
																if (cn.indexOf("⑤") != -1) {
																	az[ix] = "⑤";
																} else {
																	if (cn.indexOf("⑥") != -1) {
																		az[ix] = "⑥";
																	} else {
																		if (cn.indexOf("⑦") != -1) {
																			az[ix] = "⑦";
																		} else {
																			if (cn.indexOf("⑧") != -1) {
																				az[ix] = "⑧";
																			} else {
																				if ($("#soko").val() == 1) {
																					az[ix] = result_az[i_az].FLD_4;
																				} else {
																					az[ix] = "その他";
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
	
										}
										ix += 1;
									}
	
									var az_gk = 0;
									var az_arry = {};
									for (ix = 0; ix < az.length; ix++) {
										az_gk += 1
										var elm = az[ix];
										az_arry[elm] = (az_arry[elm] || 0) + 1;
									}
	
									var azid = [];
	
									for (var p in az) {
										if (az.hasOwnProperty(p)) {
											azid.push(p);
										}
									}
	
									var az_cnt = azid.length;
	
									if (az_cnt != 0) {
										if (meisai != 1) {
											meisai = 1;
											var tr = document.createElement('tr');
	
											var th = document.createElement('th');
											th.innerHTML = "合計出荷数";
											th.id = 'th1';
											th.style.backgroundColor = '#FF33FF';
											tr.appendChild(th);
	
											var th = document.createElement('th');
											th.innerHTML = "運送会社";
											th.id = 'th1';
											th.style.backgroundColor = '#FF33FF';
											th.width = '120';
											tr.appendChild(th);
	
											var th = document.createElement('th');
											th.innerHTML = "出荷数";
											th.id = 'th1';
											th.style.backgroundColor = '#FF33FF';
											th.width = '80';
											tr.appendChild(th);
	
											table.appendChild(tr);
	
										}
										for (var az_uns in az_arry) {
											var tr = document.createElement('tr');
	
											var td = document.createElement('td');
											td.textContent = az_gk;
											td.id = 'td1';
											td.style.backgroundColor = '#FF33FF';
											tr.appendChild(td);
	
											var td = document.createElement('td');
											td.textContent = az_uns;
											td.id = 'td1';
											td.style.backgroundColor = '#FF33FF';
											tr.appendChild(td);
	
											var td = document.createElement('td');
											td.textContent = az_arry[az_uns];
											td.id = 'td1';
											td.style.backgroundColor = '#FF33FF';
											tr.appendChild(td);
	
											table.appendChild(tr);
										}
									}
								}
							}
							
							// PLAZA 検品データ（佐川）
							if ($("#cs_as").val() == 0 || $("#cs_as").val() == 1 || $("#kubun").val() == 0 || $("#kubun").val() == 3) {
								if (result_plaza1 != null && result_plaza1.length > 0) {
									if (meisai != 1) {
										meisai = 1;
										var tr = document.createElement('tr');

										var th = document.createElement('th');
										th.innerHTML = "合計出荷数";
										th.id = 'th1';
										th.style.backgroundColor = '#00FFFF';
										tr.appendChild(th);

										var th = document.createElement('th');
										th.innerHTML = "運送会社";
										th.id = 'th1';
										th.style.backgroundColor = '#00FFFF';
										th.width = '120';
										tr.appendChild(th);

										var th = document.createElement('th');
										th.innerHTML = "出荷数";
										th.id = 'th1';
										th.style.backgroundColor = '#00FFFF';
										th.width = '80';
										tr.appendChild(th);

										table.appendChild(tr);
									}

									var tr = document.createElement('tr');

									var td = document.createElement('td');
									td.textContent = result_plaza1.length;
									td.id = 'td1';
									td.style.backgroundColor = '#00FFFF';
									tr.appendChild(td);

									var td = document.createElement('td');
									td.textContent = "プラザ佐川";
									td.id = 'td1';
									td.style.backgroundColor = '#00FFFF';
									tr.appendChild(td);

									var td = document.createElement('td');
									td.textContent = result_plaza1.length;
									td.id = 'td1';
									td.style.backgroundColor = '#00FFFF';
									tr.appendChild(td);

									table.appendChild(tr);

								}
							}
							
							// PLAZA 検品データ（Ｌ1Ｍ）
							if ($("#cs_as").val() == 0 || $("#cs_as").val() == 1 || $("#kubun").val() == 0 || $("#kubun").val() == 3) {
								if (result_plaza2 != null && result_plaza2.length > 0) {
									if (meisai != 1) {
										meisai = 1;
										var tr = document.createElement('tr');

										var th = document.createElement('th');
										th.innerHTML = "合計出荷数";
										th.id = 'th1';
										th.style.backgroundColor = '#00FF00';
										tr.appendChild(th);

										var th = document.createElement('th');
										th.innerHTML = "運送会社";
										th.id = 'th1';
										th.style.backgroundColor = '#00FF00';
										th.width = '120';
										tr.appendChild(th);

										var th = document.createElement('th');
										th.innerHTML = "出荷数";
										th.id = 'th1';
										th.style.backgroundColor = '#00FF00';
										th.width = '80';
										tr.appendChild(th);

										table.appendChild(tr);
									}

									var tr = document.createElement('tr');

									var td = document.createElement('td');
									td.textContent = result_plaza2.length;
									td.id = 'td1';
									td.style.backgroundColor = '#00FF00';
									tr.appendChild(td);

									var td = document.createElement('td');
									td.textContent = "Ｌ1Ｍ";
									td.id = 'td1';
									td.style.backgroundColor = '#00FF00';
									tr.appendChild(td);

									var td = document.createElement('td');
									td.textContent = result_plaza2.length;
									td.id = 'td1';
									td.style.backgroundColor = '#00FF00';
									tr.appendChild(td);

									table.appendChild(tr);

								}
							}
						}
					});
				}
				div.appendChild(table);
				document.body.appendChild(div);

				//バーコード描画
				if ($("#tane").val() == 0) {
					for (ix = 0; ix < tane_cnt; ix++) {
						var id = "#barcode" + ix;
						var strtane = str[ix];
						drowBarcode(id, strtane);
					}
				}

				document.getElementById("auto").innerHTML = "自動更新中・・・";
				document.frm.kbn.value = "0";
			} else {
				return false;
			}
		}
	});
}

function stp_onclick() {
	document.getElementById("auto").innerHTML = "";
	document.frm.kbn.value = "9";
}

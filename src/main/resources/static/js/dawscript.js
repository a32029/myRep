function parse_api(url) {
    $.get(url, function(data, status) {
        auth_user = data.auth_user;
        if (auth_user != null) {
            auth_bar = document.getElementById("auth_bar");
            print_auth_bar(auth_bar, auth_user)
        }

        form_data = data.form;
        form_options = data.options;
        form_method = data.method;
        if (form_data != null) {
            form = document.getElementById("form");
            print_form(form, form_data, form_options, form_method, data._links.form_url.href);
        }

        list = data.list;
        if (list != null) {
            tab = document.getElementById("tab");
            page_list(tab, list.page_items, null, null);
        }

        detail = data.detail;
        if (detail != null) {
            tab = document.getElementById("tab");
            print_detail(tab, detail);
        }

        links = data._links;
        if (links != null) {
            div = document.getElementById("div");
            print_links(div, links);
        }
    });
}
//----------------------------------------------------------------------------------------------------------------------

function print_auth_bar(auth_bar, auth_user) {
    home_href = auth_user._links.self.href;
    home_href = home_href.replace("/api", "/html");
    if (auth_user.username == "login") {
        auth_bar.appendChild(createTag(auth_user.username, home_href));
        return;
    }

    auth_bar.appendChild(createTag(auth_user.username, home_href));
    coordinator_in_course = auth_user.coordinator_in_course;

    href = auth_user._links.coordinator_in_course.href;
    href = href.replace("/api", "/html");
    if (coordinator_in_course != "") {
        auth_bar.appendChild(createTag("|", ""));
        auth_bar.appendChild(createTag(coordinator_in_course, href.replace("{0}", auth_user.coordinator_in_course)));
    }

    auth_bar.appendChild(createTag("|", ""));

    classes = auth_user.classes;
    href = auth_user._links.classes.href;
    href = href.replace("/api", "/html");
    for (x in classes) {
        auth_bar.appendChild(createTag(classes[x], href.replace("{0}", classes[x])));
    }

    if (auth_user.is_admin) {
        auth_bar.appendChild(createTag("| Management: ", ""));
        auth_bar.appendChild(createTag("Semesters", auth_user._links.list_semesters.href.replace("/api", "/html")));
        auth_bar.appendChild(createTag("Teachers", auth_user._links.list_teachers.href.replace("/api", "/html")));
        auth_bar.appendChild(createTag("Students", auth_user._links.list_students.href.replace("/api", "/html")));
    }
}

//----------------------------------------------------------------------------------------------------------------------

function print_form(form, form_data, form_options, form_method, form_url) {
    z = 0
    for (x in form_data) {
        a = document.createElement("a");
        a.innerHTML += x + ": ";
        form.appendChild(a);

        sel_option = form_options[z++];
        if (JSON.stringify(sel_option).length > 2) {
            inp = document.createElement("select");
            inp.name = x;
            for (y in sel_option) {
                option = document.createElement("option");
                option.title = sel_option[y];
                option.text = y;
                if (y == "" + form_data[x])
                    option.setAttribute('selected', 'selected');
                inp.appendChild(option);
            }
        } else {
            inp = document.createElement("input");
            inp.setAttribute("name", x);
            inp.setAttribute("type", "text");
            inp.value = form_data[x];
        }
        form.appendChild(inp);

        a = document.createElement("a");
        a.innerHTML = "<br/>";
        form.appendChild(a);
    }
    inp = document.createElement("input");
    inp.setAttribute("type", "submit");
    inp.setAttribute("name", "submit");
    inp.innerHTML = "Submit";
    form.appendChild(inp);
    form.setAttribute("method", "POST");
    form.setAttribute("enctype", "application/json");
    form.setAttribute("action", "JavaScript:submit_form(\"" + form_method + "\", \"" + form_url +"\")");
}

function submit_form(form_method, form_url) {
    var data_obj = getFormData($("#form"));

    rest_operation(form_method, form_url, data_obj, function() {
        if (this.readyState == 4) {
            if(this.status == 200) {
                alert("Operation sucess!!");
                goBack();
                return;
            }
            error = JSON.parse(this.responseText).error;
            alert("type: " + error.type + "\ntitle: " + error.title + "\nstatus: " + error.status + "\ndetail: " + error.detail + "\ninstance: " + error.instance);
        }
    });
}

function page_list(table, items, get_url, param) {
    var x = 0;
    for (var i in items) {
        var row = table.insertRow(x++);
        subitems = items[i];
        var y=0;
        for (var j in subitems) {
            get_param = null;
            if (j == param)
                get_param = subitems[j];
            var col = row.insertCell(y++);
            var a = document.createElement('a');
            a.innerHTML = subitems[j];
            col.appendChild(a);
        }
    }
}

function print_detail(table, obj) {
    for (x in obj) {
        tr = document.createElement("tr");
        th = document.createElement("th");
        th.innerHTML = x;
        tr.appendChild(th);
        td = document.createElement("td");
        td.innerHTML = obj[x];
        tr.appendChild(td);
        table.appendChild(tr);
    }
}

function print_links(div, links) {
    for (x in links) {
        p = document.createElement("p");
        a = document.createElement("a");
        a.innerHTML = x;
        a.setAttribute("href", links[x].href.replace("/api", "/html"));
        p.appendChild(a)
        div.appendChild(p);
    }
}

//----------------------------------------------------------------------------------------------------------------------

function getFormData($form){
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function(n, i){
        indexed_array[n['name']] = n['value']=="true"?true:(n['value']=="false"?false:n['value']);
    });

    return indexed_array;
}

function rest_operation(method, url, data_obj, f) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = f;
    if (method == "PUT" || method == "DELETE")
        url = formToUrl(url, data_obj);
    xhttp.open(method, url, true);
    xhttp.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    if (method == "POST") {
        json = JSON.stringify(data_obj);
        xhttp.send(json);
    } else
        xhttp.send(null);
}

function formToUrl(form_url, data_obj) {
    y = 0;
    for(x in data_obj) {
        if (y++ == 0)
            form_url += "?";
        else
            form_url += "&";
        form_url += x + "=" + data_obj[x];
    }
    return form_url;
}

function createTag(text, href) {
    a = document.createElement("a");
    a.setAttribute("class", "status_bar_text");
    if (href != "")
        a.setAttribute("href", href);
    a.innerHTML = text;
    return a;
}

function goBack() {
    history.go(-1);
    return false;
}
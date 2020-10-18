import QtQuick 2.12
import QtQuick.Controls 1.4
import QtQuick.Controls.Material 2.3
import QtQuick.Layouts 1.0
import custom.classes 1.0

TableView {
    id: view
    anchors.fill: parent


    BackEnd {
        id: backend
    }


    function fetchHello() {
        var client = new XMLHttpRequest();
        client.open("GET", "https://10.0.0.105/server/survey", true);
        client.onreadystatechange = function () {
            if (client.readyState === client.DONE) {
                print(client.responseText);
                view.model = JSON.parse(client.responseText);
            }
        }
        client.send();
    }


    itemDelegate: Item {
        Text {
            text: styleData.value
            font.pixelSize: 14
        }
    }

    // il y a des delegate qui nous permettent de changer le style
//    headerDelegate: Rectangle{
//        height: 30
//        border.width: 1
//        color: "#FAEBD7"
//        border.color: "#04f6f6"
//        Text {
//            id: headerName
//            text: styleData.value
//            font.pointSize: 15
//            font.bold: true
//            horizontalAlignment: Text.AlignHCenter
//            verticalAlignment: Text.AlignVCenter
//            anchors.fill: parent
//        }
//    }


    TableViewColumn {
        role: 'firstName'
        title: "First Name"
        width: 120
    }
    TableViewColumn {
        role: 'lastName'
        title: "Last Name"
        width: 120
    }
    TableViewColumn {
        role: 'email'
        title: "E-mail"
        width: 150
    }
    TableViewColumn {
        role: 'age'
        title: "Age"
        width: 40
    }
    TableViewColumn {
        role: 'interest'
        title: "Interest"
        width: 80
    }

}

import QtQuick 2.12
import QtQuick.Controls 1.4
import QtQuick.Controls.Material 2.3
import QtQuick.Layouts 1.0
import custom.classes 1.0

TableView {
    id: view
    anchors.fill: parent

    itemDelegate: Item {
        Text {
            text: styleData.value
            font.pixelSize: 16
            verticalAlignment: Text.AlignVCenter
            rightPadding: 10
            leftPadding: 10
            anchors.fill: parent
        }
    }

    rowDelegate:  Rectangle {
        height: 40
        property color rowColor: styleData.selected?"#FFCCC9":(styleData.alternate ? "#ffffff":"#EAEAEA")
        color: rowColor
    }

    headerDelegate: Rectangle{
        height: 40
        Text {
            id: headerName
            text: styleData.value
            font.pointSize: 12
            verticalAlignment: Text.AlignVCenter
            font.weight: Font.DemiBold
            rightPadding: 10
            leftPadding: 10
            anchors.fill: parent
        }
    }

    TableViewColumn {
        role: 'firstName'
        title: "First Name"
        width: 180
    }
    TableViewColumn {
        role: 'lastName'
        title: "Last Name"
        width: 180
    }
    TableViewColumn {
        role: 'email'
        title: "E-mail"
        width: 250
    }
    TableViewColumn {
        role: 'age'
        title: "Age"
        width: 60
    }
    TableViewColumn {
        role: 'interest'
        title: "Interest"
        width: 90
    }

}

import QtQuick 2.12
import QtQuick.Controls 2.5
import QtQuick.Controls.Material 2.3
import QtQuick.Layouts 1.0
import custom.classes 1.0

Page {
    id: settings
    width: applicationWindow.width
    height: applicationWindow.height - 50
    y: 50
    background: Rectangle {
        color: "#F5F5F6"
    }

    Label {
        id: label
        width: 145
        height: 23
        color: "#ffffff"
        text: qsTr("SETTINGS")
        anchors.top: parent.top
        horizontalAlignment: Text.AlignHCenter
        verticalAlignment: Text.AlignVCenter
        anchors.topMargin: -37
        anchors.horizontalCenterOffset: 0
        anchors.horizontalCenter: parent.horizontalCenter
        font.bold: true
        font.pointSize: 14
    }

    Frame {
        id: frame
        width: parent.width/2
        height: parent.height/3
        anchors.top: parent.top
        anchors.horizontalCenterOffset: 0
        anchors.topMargin: 20
        anchors.horizontalCenter: parent.horizontalCenter
        background: Rectangle {
            color: "transparent"
            border.color: "#7b7b7b"
            border.width: 2
            radius: 5
        }

        Label {
            id: changePassword
            x: 0
            y: 0
            text: qsTr("Change Password")
            anchors.left: parent.left
            anchors.top: parent.top
            font.pointSize: 12
            font.bold: true
        }

        Label {
            id: currentLab
            x: 0
            y: 55
            text: qsTr("Current")
            anchors.verticalCenter: rectangle2.verticalCenter
            anchors.left: parent.left
            font.pointSize: 12
        }

        Label {
            id: newPassLab
            x: 0
            y: 103
            text: qsTr("New")
            anchors.verticalCenter: rectangle1.verticalCenter
            anchors.left: parent.left
            font.pointSize: 12
        }

        Label {
            id: retypeLab
            x: 0
            y: 150
            text: qsTr("Retype new")
            anchors.verticalCenter: rectangle.verticalCenter
            anchors.left: parent.left
            font.pointSize: 12
        }

        Rectangle {
            id: rectangle
            x: 115
            y: 144
            width: 361
            height: 32
            radius: 5
            border.color: "#E1E2E1"
            border.width: 2
            anchors.right: parent.right
            anchors.bottom: parent.bottom
            clip: true
            TextField {
                id: retype
                x: 9
                y: -7
                width: 289
                height: 55
                activeFocusOnTab: true
                echoMode: TextInput.Password
            }
        }

        Rectangle {
            id: rectangle1
            x: 115
            y: 97
            width: 361
            height: 32
            radius: 5
            border.color: "#E1E2E1"
            border.width: 2
            anchors.left: rectangle.left
            anchors.right: parent.right
            anchors.horizontalCenter: rectangle.horizontalCenter
            TextField {
                id: newPass
                x: 9
                y: -7
                width: 289
                height: 55
                echoMode: TextInput.Password
                activeFocusOnTab: true
            }
            clip: true
        }

        Rectangle {
            id: rectangle2
            x: 115
            y: 49
            width: 361
            height: 32
            radius: 5
            border.color: "#E1E2E1"
            border.width: 2
            anchors.left: rectangle.left
            anchors.right: parent.right
            TextField {
                id: current
                x: 9
                y: -7
                width: 289
                height: 55
                echoMode: TextInput.Password
                activeFocusOnTab: true
            }
            anchors.horizontalCenter: rectangle.horizontalCenter
            clip: true
        }


    }

}

/*##^##
Designer {
    D{i:0;formeditorZoom:0.6600000262260437;height:600;width:1000}D{i:2}
}
##^##*/

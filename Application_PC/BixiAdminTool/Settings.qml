import QtQuick 2.12
import QtQuick.Controls 2.5
import QtQuick.Controls.Material 2.3
import QtQuick.Layouts 1.0
import custom.classes 1.0

Page {
    id: settings
    Material.accent: "#9b0000"
    Rectangle {
        width: parent.width;
        height: 50;
        color: "#9b0000"
    }

    RoundButton {
        id: roundButton
        x: 0
        y: 0
        width: 50
        height: 50
        text: "+"
        font.pointSize: 12
        flat: true
        display: AbstractButton.IconOnly
        icon.source: "back.svg"
        icon.color: "#ffffff"
        icon.width: width/1.5
        icon.height: height/1.5
        onClicked: stackView.pop()
    }



    Label {
        id: label
        x: 428
        y: 64
        width: 145
        height: 23
        color: "#ffffff"
        text: qsTr("SETTINGS")
        anchors.top: parent.top
        horizontalAlignment: Text.AlignHCenter
        verticalAlignment: Text.AlignVCenter
        anchors.topMargin: 14
        anchors.horizontalCenterOffset: 1
        anchors.horizontalCenter: parent.horizontalCenter
        font.bold: true
        font.pointSize: 14
    }

    Frame {
        id: frame
        width: 494
        x: (applicationWindow.width - width)/2
        height: 230
        anchors.top: parent.top
        anchors.topMargin: 75

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
            x: 110
            y: 125
            width: 361
            height: 32
            radius: 5
            border.color: "#E1E2E1"
            border.width: 2
            anchors.right: parent.right
            anchors.rightMargin: 0
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
            y: 87
            width: 361
            height: 32
            radius: 5
            border.color: "#E1E2E1"
            border.width: 2
            anchors.left: rectangle.left
            anchors.right: parent.right
            anchors.rightMargin: 0
            anchors.leftMargin: 0
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

        Button {
            id: apply
            x: 406
            y: 166
            height: 40
            text: qsTr("apply")
            anchors.right: parent.right
            anchors.bottom: parent.bottom
            anchors.rightMargin: 0
            anchors.bottomMargin: 0
            flat: false
            highlighted: true
        }


    }

    Frame {
        id: frame1
        x: (applicationWindow.width - width)/2
        y: 295
        width: 494
        height: 155
        anchors.top: parent.top
        Label {
            id: changeIP
            x: 0
            y: 0
            text: qsTr("Change IP Address")
            anchors.left: parent.left
            anchors.top: parent.top
            font.bold: true
            font.pointSize: 12
        }

        Label {
            id: ipLab
            x: 0
            y: 55
            text: qsTr("New Address")
            anchors.verticalCenter: rectangle5.verticalCenter
            anchors.left: parent.left
            font.pointSize: 12
        }



        Rectangle {
            id: rectangle5
            x: 115
            y: 49
            width: 361
            height: 32
            radius: 5
            border.color: "#e1e2e1"
            border.width: 2
            anchors.left: rectangle3.left
            anchors.right: parent.right
            anchors.horizontalCenter: rectangle3.horizontalCenter
            TextField {
                id: current1
                x: 9
                y: -7
                width: 289
                height: 55
                activeFocusOnTab: true
                validator:RegExpValidator {
                    regExp:/^(([01]?[0-9]?[0-9]|2([0-4][0-9]|5[0-5]))\.){3}([01]?[0-9]?[0-9]|2([0-4][0-9]|5[0-5]))$/
                }
            }
            clip: true
        }

        Button {
            id: change
            x: 401
            y: 82
            height: 40
            text: qsTr("change")
            anchors.right: parent.right
            anchors.bottom: parent.bottom
            highlighted: true
            anchors.rightMargin: 0
            anchors.bottomMargin: 0
        }
        background: Rectangle {
            color: "#00000000"
            radius: 5
            border.color: "#7b7b7b"
            border.width: 2
        }
        anchors.topMargin: 315
    }




}

/*##^##
Designer {
    D{i:0;autoSize:true;formeditorZoom:0.5;height:600;width:1000}
}
##^##*/

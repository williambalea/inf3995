import QtQuick 2.12
import QtQuick.Controls 2.5
import QtQuick.Controls.Material 2.3
import QtQuick.Layouts 1.0
import custom.classes 1.0

Page {
    id: settings
    Rectangle {
        width: parent.width;
        height: 50;
        color: "#9b0000"
    }

    Item {
        Connections {
            target: backend
            onPasswordChanged: {
                busyIndicator.visible = false;
                successLabel.visible = isSuccessful;
                wrongCurrent.visible = !isSuccessful;
            }

            onServerConnChanged: {
                if (isSuccessful) {
                    successChangeIP.visible = true;
                    backend.host = changeNewIP.text;
                    changeNewIP.text = "";
                } else {
                    errorChangeIP.visible = true;
                }
                busyIndicatorIP.visible = false;
            }
        }
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
        width: 430
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
            anchors.leftMargin: 22
            anchors.topMargin: 11
            font.pointSize: 12
            font.bold: true
        }

        Rectangle {
            id: rectangle2
            x: 115
            y: 55
            width: 361
            height: 32
            radius: 5
            border.color: "#E1E2E1"
            border.width: 2
            anchors.left: rectangle.left
            anchors.horizontalCenter: parent.horizontalCenter
            clip: true
            TextField {
                id: current
                x: 9
                y: -7
                width: 289
                height: 55
                echoMode: TextInput.Password
                activeFocusOnTab: true
                placeholderText: "Enter current password"

            }
        }

        Rectangle {
            id: rectangle1
            x: 115
            y: 93
            width: 361
            height: 32
            radius: 5
            border.color: "#E1E2E1"
            border.width: 2
            anchors.left: rectangle.left
            anchors.horizontalCenter: parent.horizontalCenter
            anchors.leftMargin: 0
            TextField {
                id: newPass
                x: 9
                y: -7
                width: 289
                height: 55
                echoMode: TextInput.Password
                activeFocusOnTab: true
                placeholderText: "Enter new password"
            }
            clip: true
        }

        Rectangle {
            id: rectangle
            x: 110
            y: 131
            width: 361
            height: 32
            radius: 5
            border.color: "#E1E2E1"
            border.width: 2
            anchors.horizontalCenter: parent.horizontalCenter
            clip: true
            TextField {
                id: retype
                x: 9
                y: -7
                width: 289
                height: 55
                activeFocusOnTab: true
                echoMode: TextInput.Password
                placeholderText: "Confirm new password"
            }
        }



        Button {
            id: apply
            x: 320
            y: 169
            height: 40
            text: qsTr("apply")
            anchors.right: parent.right
            anchors.bottom: parent.bottom
            anchors.rightMargin: 22
            anchors.bottomMargin: -3
            flat: false
            highlighted: true
            function activate() {
                successLabel.visible = false;
                twoPwMatch.visible = false;
                wrongCurrent.visible = false;

                if (newPass.text == retype.text) {
                    busyIndicator.visible = true;
                    backend.changePw(current.text, newPass.text);
                } else {
                    twoPwMatch.visible = true;
                }
            }
            onClicked: activate()
        }

        Label {
            visible: false
            id: successLabel
            x: 168
            y: 177
            width: 70
            height: 26
            color: "#dd007b22"
            text: qsTr("✔ Success")
            anchors.right: parent.right
            anchors.bottom: parent.bottom
            horizontalAlignment: Text.AlignHCenter
            verticalAlignment: Text.AlignVCenter
            anchors.bottomMargin: 3
            anchors.rightMargin: 168
            font.bold: true
            font.pointSize: 12
        }

        BusyIndicator {
            id: busyIndicator
            visible: false
            anchors.right: parent.right
            anchors.bottom: parent.bottom
            anchors.rightMargin: 93
            anchors.bottomMargin: -3
            x: 273
            y: 169
            width: 40
            height: 40
        }

        Label {
            id: twoPwMatch
            visible: false
            x: 109
            y: 179
            color: "#d52b1e"
            text: qsTr("Passwords don't match")
            anchors.bottom: parent.bottom
            horizontalAlignment: Text.AlignHCenter
            verticalAlignment: Text.AlignVCenter
            anchors.bottomMargin: 7
            font.pointSize: 12
            font.bold: true
        }

        Label {
            id: wrongCurrent
            visible: false
            x: 93
            y: 179
            color: "#d52b1e"
            text: qsTr("Wrong current password")
            anchors.bottom: parent.bottom
            horizontalAlignment: Text.AlignHCenter
            verticalAlignment: Text.AlignVCenter
            anchors.bottomMargin: 7
            font.pointSize: 12
            font.bold: true
        }


    }

    Button {
        id: logoutBtn
        x: (parent.width - width) / 2
        width: 162
        height: 48
        text: qsTr("Logout")
        anchors.top: parent.top
        highlighted: true
        anchors.topMargin: 311
        onClicked: stackView.pop(null)
    }





}

/*##^##
Designer {
    D{i:0;autoSize:true;formeditorZoom:0.6600000262260437;height:600;width:1000}D{i:20}
}
##^##*/

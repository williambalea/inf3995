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
            x: 200
            y: 173
            width: 70
            height: 26
            color: "#dd007b22"
            text: qsTr("✔ Success")
            horizontalAlignment: Text.AlignHCenter
            verticalAlignment: Text.AlignVCenter
            font.bold: true
            font.pointSize: 12
        }

        BusyIndicator {
            id: busyIndicator
            visible: false
            anchors.right: parent.right
            anchors.bottom: parent.bottom
            anchors.rightMargin: 69
            anchors.bottomMargin: 0
            x: 361
            y: 166
            width: 40
            height: 40
        }

        Label {
            id: twoPwMatch
            visible: false
            x: 107
            y: 176
            color: "#d52b1e"
            text: qsTr("The two passwords don't match")
            anchors.bottom: parent.bottom
            horizontalAlignment: Text.AlignHCenter
            verticalAlignment: Text.AlignVCenter
            anchors.bottomMargin: 10
            font.pointSize: 12
            font.bold: true
        }

        Label {
            id: wrongCurrent
            visible: false;
            x: 93
            y: 176
            color: "#d52b1e"
            text: qsTr("Current password entered is wrong")
            anchors.bottom: parent.bottom
            horizontalAlignment: Text.AlignHCenter
            verticalAlignment: Text.AlignVCenter
            anchors.bottomMargin: 10
            font.pointSize: 12
            font.bold: true
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



        Rectangle {
            id: rectangle5
            x: 0
            y: 50
            width: 361
            height: 32
            radius: 5
            border.color: "#e1e2e1"
            border.width: 2
            anchors.horizontalCenter: parent.horizontalCenter
            TextField {
                id: changeNewIP
                x: 9
                y: -7
                width: 289
                height: 55
                validator:RegExpValidator {
                    regExp:/^(([01]?[0-9]?[0-9]|2([0-4][0-9]|5[0-5]))\.){3}([01]?[0-9]?[0-9]|2([0-4][0-9]|5[0-5]))$/
                }
                placeholderText: "e.g. 10.0.0.0"
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
            onClicked: function() {
                backend.host = changeNewIP.text;
                changeNewIP.text = "";
            }
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
    D{i:0;autoSize:true;formeditorZoom:0.6600000262260437;height:600;width:1000}D{i:22}
}
##^##*/

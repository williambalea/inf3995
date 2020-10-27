import QtQuick 2.12
import QtQuick.Controls 2.5
import QtQuick.Controls.Material 2.3
import QtQuick.Layouts 1.0
import custom.classes 1.0

Page{
    id: loginPage
    width: 1280
    height: 720
    background: Rectangle {
        color: "#e66358"
    }

    Pane {
        id: pane
        x: 267
        y: 102
        width: 746
        height: 528
        Material.elevation: 4

        Text {
            id: error
            visible: backend.attemps
            x: 150
            y: 357
            width: 422
            height: 40
            color: "#d52b1e"
            text: qsTr("Wrong user and/or password")
            font.pixelSize: 22
            horizontalAlignment: Text.AlignHCenter
            verticalAlignment: Text.AlignVCenter
        }

        Button {
            id: loginButton
            x: 208
            y: 296
            width: 306
            height: 55
            text: qsTr("Login")
            font.weight: Font.Bold
            font.bold: true
            enabled: true
            highlighted: true
            flat: false
            font.pointSize: 12
            antialiasing: true
            focus: flase

            Connections {
                target: loginButton
                onClicked: function() {
                    backend.user = user.text;
                    backend.pass = pw.text;
                    backend.login();
                    backend.attemps = false;
                }
            }

        }

        Rectangle {
            x: 208
            y: 169
            border.width: 2
            border.color: "#7b7b7b"
            radius: 5
            width: 306
            height: 50

            TextInput {
                id: user
                x: 0
                y: 0
                width: parent.width
                height: parent.height
                text: qsTr("User")
                font.pixelSize: 24
                horizontalAlignment: Text.AlignLeft
                verticalAlignment: Text.AlignVCenter
                topPadding: 0
                bottomPadding: 0
                rightPadding: 5
                leftPadding: 10
                selectByMouse: true
                cursorVisible: false
                layer.smooth: true
                activeFocusOnTab: true
                focus: true
                antialiasing: tru
                color: "#000000"
            }
        }

        Rectangle {
            x: 208
            y: 233
            width: 306
            height: 50
            radius: 5
            border.color: "#7b7b7b"
            border.width: 2

            TextInput {
                id: pw
                x: 0
                y: 0
                width: parent.width
                height: parent.height
                text: qsTr("Text Input")
                font.pixelSize: 24
                horizontalAlignment: Text.AlignLeft
                verticalAlignment: Text.AlignVCenter
                rightPadding: 5
                leftPadding: 10
                antialiasing: true
                activeFocusOnTab: true
                focus: false
                autoScroll: false
                echoMode: TextInput.Password
            }
        }

        Image {
            id: image
            x: 260
            y: 27
            width: 203
            height: 100
            source: "logo.png"
            mipmap: true
            fillMode: Image.PreserveAspectFit
        }
    }
    
}



/*##^##
Designer {
    D{i:0;formeditorZoom:0.5}
}
##^##*/

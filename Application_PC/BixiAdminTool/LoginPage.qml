import QtQuick 2.12
import QtQuick.Controls 2.5
import QtQuick.Controls.Material 2.3
import QtQuick.Layouts 1.0
import custom.classes 1.0

Page{
    id: loginPage
    width: applicationWindow.width
    height: applicationWindow.height
    background: Rectangle {
        color: "#d52b1e"
    }

    Item {
        Connections {
            target: backend

            onServerConnChanged: {
                if (isSuccessful) {
                    backend.host = ip.text;
                    backend.login(user.text, pw.text);
                } else {
                    errorServerConn.visible = true;
                    progressBar.visible = false;
                }
            }

            onLoginChanged: {
                if (isSuccessful) {
                    backend.user = user.text;
                    backend.pass = pw.text;
                    stackView.push("App.qml", StackView.Immediate);
                } else {
                    errorLogin.visible = true;
                }
                progressBar.visible = false;
            }
        }
    }

    Pane {
        id: pane
        width: 700
        height: 400
        clip: true
        x: (applicationWindow.width - width)/2
        y: (applicationWindow.height - height)/2
        Material.elevation: 4
        Material.background: "#F5F5F6"

        ColumnLayout {
            x: parent.height/2
            y: 20
            height: 282

            Image {
                id: image
                x: 52
                y: 3
                source: "logo.png"
                Layout.alignment: Qt.AlignHCenter | Qt.AlignVCenter
                Layout.preferredHeight: 100
                Layout.preferredWidth: 203
                mipmap: true
                fillMode: Image.PreserveAspectFit
            }

            Rectangle {
                radius: 5
                border.color: "#7b7b7b"
                border.width: 2
                TextField {
                    id: ip
                    x: 9
                    y: -4
                    width: 289
                    height: 55
                    focus: true
                    hoverEnabled: false
                    activeFocusOnTab: true
                    clip: false
                    placeholderText: qsTr("IP address")
                    text: backend.host
                    validator:RegExpValidator {
                        regExp:/^(([01]?[0-9]?[0-9]|2([0-4][0-9]|5[0-5]))\.){3}([01]?[0-9]?[0-9]|2([0-4][0-9]|5[0-5]))$/
                    }
                }
                clip: true
                Layout.preferredWidth: 306
                Layout.preferredHeight: 40
                Layout.alignment: Qt.AlignHCenter | Qt.AlignVCenter
            }

            Rectangle {
                radius: 5
                border.color: "#7b7b7b"
                border.width: 2
                Layout.alignment: Qt.AlignHCenter | Qt.AlignVCenter
                Layout.preferredHeight: 40
                Layout.preferredWidth: 306
                clip: true

                TextField {
                    id: user
                    x: 9
                    y: -4
                    width: 289
                    height: 55
                    clip: false
                    hoverEnabled: false
                    placeholderText: qsTr("User")
                    focus: true
                    activeFocusOnTab: true
                    Keys.onReturnPressed: loginButton.activate()
                }

            }

            Rectangle {
                radius: 5
                border.color: "#7b7b7b"
                border.width: 2
                Layout.alignment: Qt.AlignHCenter | Qt.AlignVCenter
                Layout.preferredHeight: 40
                Layout.preferredWidth: 306
                clip: true
                TextField {
                    id: pw
                    x: 9
                    y: -4
                    width: 289
                    height: 55
                    placeholderText: qsTr("Password")
                    hoverEnabled: false
                    activeFocusOnTab: true
                    clip: false
                    echoMode: TextInput.Password
                    Keys.onReturnPressed: loginButton.activate()
                }
            }

            Button {
                id: loginButton
                text: qsTr("Login")
                Layout.alignment: Qt.AlignHCenter | Qt.AlignVCenter
                Layout.preferredHeight: 48
                Layout.preferredWidth: 306
                font.weight: Font.Bold
                font.bold: true
                enabled: true
                highlighted: true
                flat: false
                font.pointSize: 12
                antialiasing: true
                focus: false
                function activate() {
                    progressBar.visible = true;
                    errorServerConn.visible = false;
                    errorLogin.visible = false;
                    backend.serverConn(ip.text);
                }

                onClicked: activate()
                Keys.onReturnPressed: activate()

            }

        }
        Text {
            id: errorLogin
            color: "#d52b1e"
            text: qsTr("Wrong user and/or password")
            font.pixelSize: 16
            x: (parent.width - width)/2
            y: (parent.height - height)/2 + 130
            visible: false
        }

        ProgressBar {
            id: progressBar
            visible: false
            x: -12
            width: parent.width + 24
            anchors.bottom: parent.bottom
            indeterminate: true
            anchors.bottomMargin: -12
            Material.accent: "#9b0000"
        }

        Text {
            id: errorServerConn
            x: (parent.width - width)/2
            y: (parent.height - height)/2 + 130
            visible: false
            color: "#d52b1e"
            text: qsTr("Can't connect to server")
            font.pixelSize: 16
        }
    }

}



/*##^##
Designer {
    D{i:0;formeditorZoom:0.6600000262260437;height:600;width:1000}
}
##^##*/

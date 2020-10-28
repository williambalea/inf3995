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

    Pane {
        id: pane
        width: 700
        height: 400
        x: (applicationWindow.width - width)/2
        y: (applicationWindow.height - height)/2
        Material.elevation: 4
        Material.background: "#F5F5F6"

        ColumnLayout {
            x: parent.height/2
            y: 50

            Image {
                id: image
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
                    backend.user = user.text;
                    backend.pass = pw.text;
                    backend.login();
                    backend.attemps = false;

                }

                onClicked: activate()
                Keys.onReturnPressed: activate()

            }
        }
        Text {
            id: error
            visible: backend.attemps
            color: "#d52b1e"
            text: qsTr("Wrong user and/or password")
            font.pixelSize: 16
            x: (parent.width - width)/2
            y: (parent.height - height)/2 + 130
        }
    }

}



/*##^##
Designer {
    D{i:0;formeditorZoom:0.5;height:600;width:1000}
}
##^##*/

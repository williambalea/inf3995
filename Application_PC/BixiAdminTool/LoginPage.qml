import QtQuick 2.12
import QtQuick.Controls 2.5
import QtQuick.Controls.Material 2.3
import QtQuick.Layouts 1.0
import custom.classes 1.0

Page{
    id : loginPage
    width: parent.width
    height: parent.height

    Label {
        id: label3
        x: 435
        y: 416
        text: qsTr("USER")
        font.pointSize: 24
    }
    
    Label {
        id: label4
        x: 435
        y: 461
        text: qsTr("PASSWORD")
        font.pointSize: 24
    }

    TextInput {
        id: user
        x: 620
        y: 416
        width: 306
        height: 39
        text: qsTr("Text Input")
        font.pixelSize: 24
        horizontalAlignment: Text.AlignLeft
        verticalAlignment: Text.AlignVCenter
    }
    
    TextInput {
        id: pw
        x: 620
        y: 461
        width: 306
        height: 39
        text: qsTr("Text Input")
        font.pixelSize: 24
        horizontalAlignment: Text.AlignLeft
        verticalAlignment: Text.AlignVCenter
        autoScroll: false
        echoMode: TextInput.Password
    }

    Button {
        id: loginButton
        x: 606
        y: 530
        text: qsTr("Login")

        Connections {
            target: loginButton
            onClicked: function() {
                if (user.text === "admin" && pw.text === "admin") {
                    login.visible = false;
                    app.visible = true;
                }
            }
        }
    }
    
}

/*##^##
Designer {
    D{i:0;formeditorZoom:0.5;height:960;width:1280}
}
##^##*/

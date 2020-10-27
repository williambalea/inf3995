import QtQuick 2.12
import QtQuick.Controls 2.5
import QtQuick.Controls.Material 2.3
import QtQuick.Layouts 1.0
import custom.classes 1.0

Page {
    id: engine1Page
    width: applicationWindow.width
    height: applicationWindow.height - 86
    
    TextArea {
        id: textArea
        x: 30
        y: 30
        width: parent.width - 60
        height: parent.height - 60
        verticalAlignment: Text.AlignBottom
        placeholderText: qsTr("Text Area")
        background: Rectangle {
            color: "#F5F5F6"
        }
    }
    
    background: Rectangle {
        color: "#E1E2E1"
    }
}

/*##^##
Designer {
    D{i:0;formeditorZoom:0.33000001311302185;height:720;width:1280}
}
##^##*/

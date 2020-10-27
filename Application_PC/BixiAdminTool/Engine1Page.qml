import QtQuick 2.12
import QtQuick.Controls 2.5
import QtQuick.Controls.Material 2.3
import QtQuick.Layouts 1.0
import custom.classes 1.0

Page {
    id: engine1Page
    width: 1280
    height: 633
    
    TextArea {
        id: textArea
        x: 28
        y: 29
        width: 1223
        height: 570
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
    D{i:0;formeditorZoom:0.5}
}
##^##*/

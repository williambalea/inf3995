import QtQuick 2.12
import QtQuick.Controls 2.5
import QtQuick.Controls.Material 2.3
import QtQuick.Layouts 1.0
import custom.classes 1.0

Page {
    id: engine1Page
    width: applicationWindow.width
    height: applicationWindow.height - 86

    BackEnd {

        onLog1Changed: {
            elements.insert(0, {logText: log + log + log + log + log});
        }
    }
    
    Rectangle {
        x: 30
        y: 30
        width: parent.width - 60
        height: parent.height - 60
        transformOrigin: Rectangle.Center


        ListView {
            id: listView
            anchors.fill: parent
            spacing: 2
            boundsBehavior: Flickable.StopAtBounds
            clip: true
            ScrollBar.vertical: ScrollBar {}
            model: ListModel {
                id: elements

            }

            delegate: Text{
                height: 20
                text: logText
                verticalAlignment: Text.AlignVCenter
                wrapMode: Text.WordWrap
                clip: true


            }
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

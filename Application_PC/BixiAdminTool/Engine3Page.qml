import QtQuick 2.12
import QtQuick.Controls 2.5
import QtQuick.Controls.Material 2.3
import QtQuick.Layouts 1.0
import custom.classes 1.0


Page {
    id: engine3Page
    width: applicationWindow.width
    height: applicationWindow.height - 86
    
    Rectangle {
        id: listViewContenant
        x: 5
        y: 5
        width: parent.width - 10
        height: parent.height - 10
        radius: 5
        
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
            
            delegate: Rectangle {
                id: rectangle
                width: listViewContenant.width
                height: childrenRect.height
                clip: true
                border.color: "#7b7b7b"
                border.width: 2
                radius: 5
                Text{
                    width: parent.width
                    padding: 5
                    text: logText
                    wrapMode: Text.WordWrap
                }
                Rectangle {
                    id: rectangle1
                    visible: (typ === 1)
                    width: (typ === 0) ? 0 : 500
                    height: (typ === 0) ? 0 : 500
                    color: "transparent"
                    Image {
                        source: image
                        anchors.leftMargin: 5
                        anchors.bottomMargin: 5
                        anchors.topMargin: 5
                        fillMode: Image.PreserveAspectFit
                        width: 490
                        anchors.left: parent.left
                        anchors.top: parent.top
                        anchors.bottom: parent.bottom
                    }
                }
            }
        }
    }
    
    background: Rectangle {
        color: "#E1E2E1"
    }
}

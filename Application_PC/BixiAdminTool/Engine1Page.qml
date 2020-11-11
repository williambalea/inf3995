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
        function createRect() {
            var component = Qt.createComponent("MyComponent.qml");
            var rect = component.createObject(iiii);
            if(rect !== null ) {
                rect.name = "Test";
                rect.x =  Math.floor(Math.random() * 3000 );
                rect.y = Math.floor(Math.random() * 1000 );
            }
        }

        onLog1Changed: {
            createRect()
        }
    }
    
    ScrollView {
        id: scrollView
        x: 30
        y: 30
        width: parent.width - 60
        height: parent.height - 60
        clip: true
        Item {
            id: iiii
        }

        Text {
            id: ab
            text: qsTr("absdfgsdfgfsfdgsgrg")
            font.pointSize: 400

        }


    }
    
    background: Rectangle {
        color: "#E1E2E1"
    }
}

/*##^##
Designer {
    D{i:0;formeditorZoom:0.20000000298023224;height:720;width:1280}
}
##^##*/

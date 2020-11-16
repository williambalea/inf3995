#ifndef BACKEND_H
#define BACKEND_H

#include <QObject>
#include <QString>
#include <qqml.h>
#include <QNetworkAccessManager>
#include <QNetworkReply>

class BackEnd : public QObject
{
    Q_OBJECT
    Q_PROPERTY(QString host MEMBER m_host NOTIFY hostChanged)
    Q_PROPERTY(QString user MEMBER m_user)
    Q_PROPERTY(QString pass MEMBER m_pass)
    Q_PROPERTY(QString engine1Logs MEMBER m_engine1Logs)
    Q_PROPERTY(bool engine1Status MEMBER m_engine1Status NOTIFY enginesStatusChanged)
    Q_PROPERTY(bool engine2Status MEMBER m_engine2Status NOTIFY enginesStatusChanged)
    Q_PROPERTY(bool engine3Status MEMBER m_engine3Status NOTIFY enginesStatusChanged)
    Q_PROPERTY(QString sqlData READ sqlData WRITE setSqlData NOTIFY sqlDataChanged)
    QML_ELEMENT
public:
    explicit BackEnd(QObject *parent = nullptr);
    ~BackEnd();

    QString sqlData();
    void setSqlData(const QString &data);

    Q_INVOKABLE void refresh();
    Q_INVOKABLE void login(QString user, QString pass);
    Q_INVOKABLE void changePw(QString old, QString newPass);
    Q_INVOKABLE void serverConn(QString host);

signals:
    void hostChanged();
    void sqlDataChanged();
    void enginesStatusChanged();
    void log1Changed(QString log);
    void loginChanged(bool isSuccessful);
    void passwordChanged(bool isSuccessful);
    void serverConnChanged(bool isSuccessful);

private slots:
    void sqlFinished(QNetworkReply *reply);
    void loginFinished(QNetworkReply *reply);
    void checkEnginesFinished(QNetworkReply *reply);
    void changePwFinished(QNetworkReply *reply);
    void serverConnFinished(QNetworkReply *reply);
    void periodicFn();

private:
    void setupNetworkManagers();
    QNetworkRequest makeRequest(const QUrl &url);
    void checkEngines();
    void sendFakeLogs();

    QString m_sqlData = "";
    QNetworkAccessManager *manSqlData;
    QNetworkAccessManager *manLogin;
    QNetworkAccessManager *manEnginesStatus;
    QNetworkAccessManager *manChangePw;
    QNetworkAccessManager *manServerConn;
    QTimer *timer;
    bool m_engine1Status = false;
    bool m_engine2Status = false;
    bool m_engine3Status = false;
    QString m_engine1Logs = "";
    QString m_host = "10.0.0.105";
    QString m_user = "";
    QString m_pass = "";

};

#endif // BACKEND_H

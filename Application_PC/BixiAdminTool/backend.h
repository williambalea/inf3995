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
    Q_PROPERTY(bool attemps MEMBER attemps NOTIFY attempsChanged)
    Q_PROPERTY(QString host MEMBER m_host NOTIFY hostChanged)
    Q_PROPERTY(QString engine1Logs MEMBER m_engine1Logs)
    Q_PROPERTY(bool engine1Status MEMBER m_engine1Status NOTIFY enginesStatusChanged)
    Q_PROPERTY(bool engine2Status MEMBER m_engine2Status NOTIFY enginesStatusChanged)
    Q_PROPERTY(bool engine3Status MEMBER m_engine3Status NOTIFY enginesStatusChanged)
    Q_PROPERTY(QString sqlData READ sqlData WRITE setSqlData NOTIFY sqlDataChanged)
    Q_PROPERTY(bool isLoggedIn READ isLoggedIn NOTIFY isLoggedInChanged)
    QML_ELEMENT
public:
    explicit BackEnd(QObject *parent = nullptr);
    ~BackEnd();

    QString sqlData();
    bool isLoggedIn();
    void setSqlData(const QString &data);

    Q_INVOKABLE void refresh();
    Q_INVOKABLE void login(QString user, QString pass);

signals:
    void attempsChanged();
    void hostChanged();
    void sqlDataChanged();
    void isLoggedInChanged();
    void enginesStatusChanged();
    void log1Changed(QString log);

private slots:
    void sqlFinished(QNetworkReply *reply);
    void loginFinished(QNetworkReply *reply);
    void checkEnginesFinished(QNetworkReply *reply);
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
    QTimer *timer;
    bool m_isLoggedIn = false;
    bool attemps = false;
    QString m_host = "10.0.0.105";
    bool m_engine1Status = false;
    bool m_engine2Status = false;
    bool m_engine3Status = false;
    QString m_engine1Logs = "";

};

#endif // BACKEND_H

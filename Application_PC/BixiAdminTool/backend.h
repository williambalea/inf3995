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
    Q_PROPERTY(bool engine1Status MEMBER m_engine1Status NOTIFY enginesStatusChanged)
    Q_PROPERTY(bool engine2Status MEMBER m_engine2Status NOTIFY enginesStatusChanged)
    Q_PROPERTY(bool engine3Status MEMBER m_engine3Status NOTIFY enginesStatusChanged)
    Q_PROPERTY(QString sqlData READ sqlData WRITE setSqlData NOTIFY sqlDataChanged)
    Q_PROPERTY(bool isLoggedIn READ isLoggedIn NOTIFY isLoggedInChanged)
    Q_PROPERTY(QString user READ user WRITE setUser NOTIFY userChanged)
    Q_PROPERTY(QString pass READ user WRITE setPass NOTIFY passChanged)
    QML_ELEMENT
public:
    explicit BackEnd(QObject *parent = nullptr);
    ~BackEnd();

    QString sqlData();
    bool isLoggedIn();
    QString user();
    QString pass();
    void setUser(const QString &data);
    void setPass(const QString &data);
    void setSqlData(const QString &data);

    Q_INVOKABLE void refresh();
    Q_INVOKABLE void login();

signals:
    void attempsChanged();
    void hostChanged();
    void sqlDataChanged();
    void isLoggedInChanged();
    void userChanged();
    void passChanged();
    void enginesStatusChanged();

private slots:
    void sqlFinished(QNetworkReply *reply);
    void loginFinished(QNetworkReply *reply);
    void periodicFn();
    void checkEnginesFinished(QNetworkReply *reply);

private:
    void setupNetworkManagers();
    QNetworkRequest makeRequest(const QUrl &url);
    void checkEngines();

    QString m_sqlData = "";
    QString m_user = "";
    QString m_pass = "";
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

};

#endif // BACKEND_H

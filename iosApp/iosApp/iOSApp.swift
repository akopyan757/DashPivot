import SwiftUI
import ComposeApp
import Combine

@main
struct iOSApp: App {
    @StateObject private var tokenHolder = TokenHolder()

    init() {
        InitKoinKt.InitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView(tokenHolder: tokenHolder)
                .onOpenURL { url in
                    handleDeepLink(url: url)
                }
        }
    }

    private func handleDeepLink(url: URL) {
        if url.scheme == "dashpivot" && url.host == "verify" {
            let token = url.queryParameters?["token"]
            print("iOSApp: Token received from deep link: \(token ?? "no token")")
            tokenHolder.token = token
        }
    }
}

extension URL {
    var queryParameters: [String: String]? {
        var params = [String: String]()
        URLComponents(url: self, resolvingAgainstBaseURL: false)?.queryItems?.forEach {
            params[$0.name] = $0.value
        }
        return params
    }
}

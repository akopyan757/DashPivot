import Foundation
import Combine

class TokenHolder: ObservableObject {
    @Published var token: String?
}
